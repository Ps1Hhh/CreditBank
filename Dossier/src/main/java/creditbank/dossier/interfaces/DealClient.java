package creditbank.dossier.interfaces;


import creditbank.dossier.config.FeignConfig;
import creditbank.dossier.exception.CustomErrorDecoder;
import creditbank.dossier.exception.DefaultException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "deal", configuration = {FeignConfig.class, CustomErrorDecoder.class})
    public interface DealClient {

        @RequestMapping(method = RequestMethod.POST, value = "/document/{statementId}/status")
        void changeStatementStatusOnDocumentsCreation(@PathVariable("statementId") String statementId) throws DefaultException;

    }
