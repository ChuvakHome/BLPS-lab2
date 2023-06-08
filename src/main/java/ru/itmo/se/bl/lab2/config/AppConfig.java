package ru.itmo.se.bl.lab2.config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import ru.itmo.se.bl.lab2.adapter.jakarta.JakartaTransactionManager;
import ru.itmo.se.bl.lab2.adapter.jakarta.JakartaUserTransaction;
import ru.itmo.se.bl.lab2.data.UsersXmlCollection;

import java.time.Duration;

@Configuration
@EnableTransactionManagement
public class AppConfig {
    private BitronixTransactionManager bitronixTransactionManager;

    @Bean()
    public TransactionManager bitronixTransactionManager() throws Throwable {
        if (bitronixTransactionManager == null) {
            bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
            bitronixTransactionManager.setTransactionTimeout((int) Duration.ofMinutes(10).toSeconds());
        }

        return new JakartaTransactionManager(bitronixTransactionManager);
    }

//    @Bean
//    public UserTransaction bitronixUserTransaction() throws Throwable {
//        if (bitronixTransactionManager == null) {
//            BitronixTransactionManager bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
//            bitronixTransactionManager.setTransactionTimeout((int) Duration.ofMinutes(10).toSeconds());
//        }
//
//        return new JakartaUserTransaction(bitronixTransactionManager);
//    }

    @Bean
    public UsersXmlCollection usersCollection() {
        return new UsersXmlCollection();
    }

    @Bean
    public PlatformTransactionManager transactionManager(TransactionManager transactionManager) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(transactionManager);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);

        return jtaTransactionManager;
    }
}
