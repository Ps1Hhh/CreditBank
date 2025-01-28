package creditbank.deal.service;

import creditbank.deal.model.entity.Statement;
import creditbank.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final StatementRepository statementRepository;

    public Statement getStatementById(String statementId) {
        return statementRepository.getByStatementId(UUID.fromString(statementId));
    }

    public List<Statement> getAllStatements() {
        return statementRepository.findAll();
    }
}
