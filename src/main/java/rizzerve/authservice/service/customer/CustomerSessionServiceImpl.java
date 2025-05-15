package rizzerve.authservice.service.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.customer.CustomerSessionRequest;
import rizzerve.authservice.dto.customer.CustomerSessionResponse;
import rizzerve.authservice.dto.customer.EndSessionRequest;
import rizzerve.authservice.exception.TableAlreadyOccupiedException;
import rizzerve.authservice.exception.TableNotFoundException;
import rizzerve.authservice.exception.SessionNotFoundException;
import rizzerve.authservice.model.CustomerSession;
import rizzerve.authservice.repository.CustomerSessionRepository;
import rizzerve.authservice.security.token.TokenService;
import rizzerve.authservice.service.integration.TableServiceIntegration;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerSessionServiceImpl implements CustomerSessionService {
    private final CustomerSessionRepository sessionRepository;
    private final TokenService tokenService;
    private final TableServiceIntegration tableServiceIntegration;

    @Override
    public CustomerSessionResponse createSession(CustomerSessionRequest request) {
        Integer tableNumber = request.getTableNumber();

        Optional<CustomerSession> existingSession = sessionRepository
                .findByTableNumberAndActive(tableNumber, true);

        if (existingSession.isPresent()) {
            throw new TableAlreadyOccupiedException("Table " + tableNumber + " is already occupied");
        }

        if (!tableServiceIntegration.isTableAvailable(tableNumber)) {
            throw new TableNotFoundException("Table " + tableNumber + " is not available");
        }

        tableServiceIntegration.updateTableStatus(tableNumber, "TERPAKAI");

        String sessionToken = tokenService.generateSessionToken(tableNumber);

        CustomerSession session = CustomerSession.builder()
                .tableNumber(tableNumber)
                .sessionToken(sessionToken)
                .startTime(LocalDateTime.now())
                .active(true)
                .build();

        CustomerSession savedSession = sessionRepository.save(session);

        return CustomerSessionResponse.builder()
                .status("success")
                .sessionId(savedSession.getId())
                .sessionToken(sessionToken)
                .tableNumber(tableNumber)
                .startTime(savedSession.getStartTime())
                .build();
    }

    @Override
    public void endSession(EndSessionRequest request) {
        CustomerSession session = sessionRepository.findBySessionToken(request.getSessionToken())
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        session.setEndTime(LocalDateTime.now());
        session.setActive(false);
        sessionRepository.save(session);

        tableServiceIntegration.updateTableStatus(session.getTableNumber(), "TERSEDIA");
    }

    @Override
    public boolean validateSession(String sessionToken) {
        if (!tokenService.validateSessionToken(sessionToken)) {
            return false;
        }

        Optional<CustomerSession> session = sessionRepository.findBySessionToken(sessionToken);
        return session.isPresent() && session.get().isActive();
    }

    @Override
    public Integer getTableNumberFromSession(String sessionToken) {
        if (!validateSession(sessionToken)) {
            return null;
        }

        return tokenService.extractTableNumber(sessionToken);
    }
}
