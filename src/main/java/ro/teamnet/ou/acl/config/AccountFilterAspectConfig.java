package ro.teamnet.ou.acl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ro.teamnet.ou.acl.aop.AccountFilterAspect;

/**
 * Created by Ionut.Patrascu on 9/4/2015.
 */

@Configuration
@EnableAspectJAutoProxy
public class AccountFilterAspectConfig {

    @Bean
    public AccountFilterAspect accountFilterAspect(){
        return new AccountFilterAspect();
    }
}
