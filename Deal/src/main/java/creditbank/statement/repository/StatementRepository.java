package creditbank.statement.repository;

import creditbank.statement.model.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<Statement, UUID> {

    Statement getByStatementId(UUID uuid);
}