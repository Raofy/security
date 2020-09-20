# shiro框架

#### 快速入门

- 添加依赖

    ```xml
    <!--shiro依赖-->
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-web</artifactId>
        <version>1.4.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-spring</artifactId>
        <version>1.4.0</version>
    </dependency>
    ```
  
- 配置

    a. log4j日志配置
    
    ```properties
  log4j.rootLogger=INFO, stdout
  
  log4j.appender.stdout=org.apache.log4j.ConsoleAppender
  log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
  log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m %n
  
  # General Apache libraries
  log4j.logger.org.apache=WARN
  
  # Spring
  log4j.logger.org.springframework=WARN
  
  # Default Shiro logging
  log4j.logger.org.apache.shiro=INFO
  
  # Disable verbose logging
  log4j.logger.org.apache.shiro.util.ThreadContext=WARN
  log4j.logger.org.apache.shiro.cache.ehcache.EhCache=WARN
   ```
  
    b. shiro配置文件
    
    ```ini
    [users]
    # user 'root' with password 'secret' and the 'admin' role
    root = secret, admin
    # user 'guest' with the password 'guest' and the 'guest' role
    guest = guest, guest
    # user 'presidentskroob' with password '12345' ("That's the same combination on
    # my luggage!!!" ;)), and role 'president'
    presidentskroob = 12345, president
    # user 'darkhelmet' with password 'ludicrousspeed' and roles 'darklord' and 'schwartz'
    darkhelmet = ludicrousspeed, darklord, schwartz
    # user 'lonestarr' with password 'vespa' and roles 'goodguy' and 'schwartz'
    lonestarr = vespa, goodguy, schwartz
    
    # -----------------------------------------------------------------------------
    # Roles with assigned permissions
    #
    # Each line conforms to the format defined in the
    # org.apache.shiro.realm.text.TextConfigurationRealm#setRoleDefinitions JavaDoc
    # -----------------------------------------------------------------------------
    [roles]
    # 'admin' role has all permissions, indicated by the wildcard '*'
    admin = *
    # The 'schwartz' role can do anything (*) with any lightsaber:
    schwartz = lightsaber:*
    # The 'goodguy' role is allowed to 'drive' (action) the winnebago (type) with
    # license plate 'eagle5' (instance specific id)
    goodguy = winnebago:drive:eagle5
    ```
  
    c. QuickStart
    
    ```java
    package com.ryan.shiro.sample;
    
    import org.apache.shiro.SecurityUtils;
    import org.apache.shiro.authc.*;
    import org.apache.shiro.config.IniSecurityManagerFactory;
    import org.apache.shiro.mgt.SecurityManager;
    import org.apache.shiro.session.Session;
    import org.apache.shiro.subject.Subject;
    import org.apache.shiro.util.Factory;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    
    public class QuickStart {
    
        private static final transient Logger log = LoggerFactory.getLogger(QuickStart.class);
    
    
        public static void main(String[] args) {
    
            //使用工厂模式读取resources目录下的shiro.ini配置文件，并返回一个securityManager实例，并设置SecurityManager
            Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
            SecurityManager securityManager = factory.getInstance();
            SecurityUtils.setSecurityManager(securityManager);
    
            // Now that a simple Shiro environment is set up, let's see what you can do:
    
            // get the currently executing user:
            //获取当前Security执行对象，Subject
            Subject currentUser = SecurityUtils.getSubject();
    
            // 获取当前Security对象
            Session session = currentUser.getSession();
            session.setAttribute("someKey", "aValue");
            String value = (String) session.getAttribute("someKey");
            if (value.equals("aValue")) {
                log.info("存储当前的值! [" + value + "]");
            }
    
            // 判断当前用户是否被认证
            if (!currentUser.isAuthenticated()) {
                UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
                token.setRememberMe(true);
                try {
                    currentUser.login(token);
                } catch (UnknownAccountException uae) {
                    log.info("There is no user with username of " + token.getPrincipal());
                } catch (IncorrectCredentialsException ice) {
                    log.info("Password for account " + token.getPrincipal() + " was incorrect!");
                } catch (LockedAccountException lae) {
                    log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                            "Please contact your administrator to unlock it.");
                }
                // ... catch more exceptions here (maybe custom ones specific to your application?
                catch (AuthenticationException ae) {
                    //unexpected condition?  error?
                }
            }
    
            //say who they are:
            //print their identifying principal (in this case, a username):
            log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
    
            //判断用户的授权权限
            if (currentUser.hasRole("schwartz")) {
                log.info("May the Schwartz be with you!");
            } else {
                log.info("Hello, mere mortal.");
            }
    
            //test a typed permission (not instance-level)
            if (currentUser.isPermitted("lightsaber:wield")) {
                log.info("You may use a lightsaber ring.  Use it wisely.");
            } else {
                log.info("Sorry, lightsaber rings are for schwartz masters only.");
            }
    
            //当前用户是否被允许
            if (currentUser.isPermitted("winnebago:drive:eagle5")) {
                log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                        "Here are the keys - have fun!");
            } else {
                log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
            }
    
            //当前用户退出
            currentUser.logout();
    
            System.exit(0);
        }
    }

    ```
  
    d. 结果输出
    
    ```text
    "C:\Program Files\Java\jdk1.8.0_261\bin\java.exe" "-javaagent:J:\install\IntelliJ IDEA 2019.2.4\lib\idea_rt.jar=56499:J:\install\IntelliJ IDEA 2019.2.4\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_261\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_261\jre\lib\rt.jar;U:\Ryan\Blog\security\shiro\target\classes;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot-starter-thymeleaf\2.3.4.RELEASE\spring-boot-starter-thymeleaf-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot-starter\2.3.4.RELEASE\spring-boot-starter-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot\2.3.4.RELEASE\spring-boot-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\2.3.4.RELEASE\spring-boot-autoconfigure-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot-starter-logging\2.3.4.RELEASE\spring-boot-starter-logging-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;C:\Users\小黑\.m2\repository\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;C:\Users\小黑\.m2\repository\org\apache\logging\log4j\log4j-to-slf4j\2.13.3\log4j-to-slf4j-2.13.3.jar;C:\Users\小黑\.m2\repository\org\apache\logging\log4j\log4j-api\2.13.3\log4j-api-2.13.3.jar;C:\Users\小黑\.m2\repository\org\slf4j\jul-to-slf4j\1.7.30\jul-to-slf4j-1.7.30.jar;C:\Users\小黑\.m2\repository\jakarta\annotation\jakarta.annotation-api\1.3.5\jakarta.annotation-api-1.3.5.jar;C:\Users\小黑\.m2\repository\org\yaml\snakeyaml\1.26\snakeyaml-1.26.jar;C:\Users\小黑\.m2\repository\org\thymeleaf\thymeleaf-spring5\3.0.11.RELEASE\thymeleaf-spring5-3.0.11.RELEASE.jar;C:\Users\小黑\.m2\repository\org\thymeleaf\thymeleaf\3.0.11.RELEASE\thymeleaf-3.0.11.RELEASE.jar;C:\Users\小黑\.m2\repository\org\attoparser\attoparser\2.0.5.RELEASE\attoparser-2.0.5.RELEASE.jar;C:\Users\小黑\.m2\repository\org\unbescape\unbescape\1.1.6.RELEASE\unbescape-1.1.6.RELEASE.jar;C:\Users\小黑\.m2\repository\org\thymeleaf\extras\thymeleaf-extras-java8time\3.0.4.RELEASE\thymeleaf-extras-java8time-3.0.4.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot-starter-web\2.3.4.RELEASE\spring-boot-starter-web-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot-starter-json\2.3.4.RELEASE\spring-boot-starter-json-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.11.2\jackson-databind-2.11.2.jar;C:\Users\小黑\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.11.2\jackson-annotations-2.11.2.jar;C:\Users\小黑\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.11.2\jackson-core-2.11.2.jar;C:\Users\小黑\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.11.2\jackson-datatype-jdk8-2.11.2.jar;C:\Users\小黑\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.11.2\jackson-datatype-jsr310-2.11.2.jar;C:\Users\小黑\.m2\repository\com\fasterxml\jackson\module\jackson-module-parameter-names\2.11.2\jackson-module-parameter-names-2.11.2.jar;C:\Users\小黑\.m2\repository\org\springframework\boot\spring-boot-starter-tomcat\2.3.4.RELEASE\spring-boot-starter-tomcat-2.3.4.RELEASE.jar;C:\Users\小黑\.m2\repository\org\apache\tomcat\embed\tomcat-embed-core\9.0.38\tomcat-embed-core-9.0.38.jar;C:\Users\小黑\.m2\repository\org\glassfish\jakarta.el\3.0.3\jakarta.el-3.0.3.jar;C:\Users\小黑\.m2\repository\org\apache\tomcat\embed\tomcat-embed-websocket\9.0.38\tomcat-embed-websocket-9.0.38.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-web\5.2.9.RELEASE\spring-web-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-beans\5.2.9.RELEASE\spring-beans-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-webmvc\5.2.9.RELEASE\spring-webmvc-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-aop\5.2.9.RELEASE\spring-aop-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-context\5.2.9.RELEASE\spring-context-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-expression\5.2.9.RELEASE\spring-expression-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-core\5.2.9.RELEASE\spring-core-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\springframework\spring-jcl\5.2.9.RELEASE\spring-jcl-5.2.9.RELEASE.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-core\1.6.0\shiro-core-1.6.0.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-lang\1.6.0\shiro-lang-1.6.0.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-cache\1.6.0\shiro-cache-1.6.0.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-crypto-hash\1.6.0\shiro-crypto-hash-1.6.0.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-crypto-core\1.6.0\shiro-crypto-core-1.6.0.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-crypto-cipher\1.6.0\shiro-crypto-cipher-1.6.0.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-config-core\1.6.0\shiro-config-core-1.6.0.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-config-ogdl\1.6.0\shiro-config-ogdl-1.6.0.jar;C:\Users\小黑\.m2\repository\commons-beanutils\commons-beanutils\1.9.4\commons-beanutils-1.9.4.jar;C:\Users\小黑\.m2\repository\commons-collections\commons-collections\3.2.2\commons-collections-3.2.2.jar;C:\Users\小黑\.m2\repository\org\apache\shiro\shiro-event\1.6.0\shiro-event-1.6.0.jar;C:\Users\小黑\.m2\repository\org\slf4j\jcl-over-slf4j\1.7.30\jcl-over-slf4j-1.7.30.jar;C:\Users\小黑\.m2\repository\org\slf4j\slf4j-api\1.7.30\slf4j-api-1.7.30.jar;C:\Users\小黑\.m2\repository\org\slf4j\slf4j-log4j12\1.7.30\slf4j-log4j12-1.7.30.jar;C:\Users\小黑\.m2\repository\log4j\log4j\1.2.17\log4j-1.2.17.jar" com.ryan.shiro.sample.QuickStart
    SLF4J: Class path contains multiple SLF4J bindings.
    SLF4J: Found binding in [jar:file:/C:/Users/%e5%b0%8f%e9%bb%91/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: Found binding in [jar:file:/C:/Users/%e5%b0%8f%e9%bb%91/.m2/repository/org/slf4j/slf4j-log4j12/1.7.30/slf4j-log4j12-1.7.30.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
    SLF4J: Actual binding is of type [ch.qos.logback.classic.util.ContextSelectorStaticBinder]
    17:37:38.620 [main] DEBUG org.apache.shiro.io.ResourceUtils - Opening resource from class path [shiro.ini]
    17:37:38.636 [main] DEBUG org.apache.shiro.config.Ini - Parsing [users]
    17:37:38.639 [main] DEBUG org.apache.shiro.config.Ini - Parsing [roles]
    17:37:38.664 [main] DEBUG org.apache.commons.beanutils.converters.BooleanConverter - Setting default value: false
    17:37:38.664 [main] DEBUG org.apache.commons.beanutils.converters.BooleanConverter - Converting 'Boolean' value 'false' to type 'Boolean'
    17:37:38.665 [main] DEBUG org.apache.commons.beanutils.converters.BooleanConverter -     No conversion required, value is already a Boolean
    17:37:38.667 [main] DEBUG org.apache.commons.beanutils.converters.ByteConverter - Setting default value: 0
    17:37:38.667 [main] DEBUG org.apache.commons.beanutils.converters.ByteConverter - Converting 'Integer' value '0' to type 'Byte'
    17:37:38.667 [main] DEBUG org.apache.commons.beanutils.converters.ByteConverter -     Converted to Byte value '0'
    17:37:38.668 [main] DEBUG org.apache.commons.beanutils.converters.CharacterConverter - Setting default value:  
    17:37:38.668 [main] DEBUG org.apache.commons.beanutils.converters.CharacterConverter - Converting 'Character' value ' ' to type 'Character'
    17:37:38.668 [main] DEBUG org.apache.commons.beanutils.converters.CharacterConverter -     No conversion required, value is already a Character
    17:37:38.669 [main] DEBUG org.apache.commons.beanutils.converters.DoubleConverter - Setting default value: 0
    17:37:38.669 [main] DEBUG org.apache.commons.beanutils.converters.DoubleConverter - Converting 'Integer' value '0' to type 'Double'
    17:37:38.669 [main] DEBUG org.apache.commons.beanutils.converters.DoubleConverter -     Converted to Double value '0.0'
    17:37:38.669 [main] DEBUG org.apache.commons.beanutils.converters.FloatConverter - Setting default value: 0
    17:37:38.669 [main] DEBUG org.apache.commons.beanutils.converters.FloatConverter - Converting 'Integer' value '0' to type 'Float'
    17:37:38.669 [main] DEBUG org.apache.commons.beanutils.converters.FloatConverter -     Converted to Float value '0.0'
    17:37:38.670 [main] DEBUG org.apache.commons.beanutils.converters.IntegerConverter - Setting default value: 0
    17:37:38.670 [main] DEBUG org.apache.commons.beanutils.converters.IntegerConverter - Converting 'Integer' value '0' to type 'Integer'
    17:37:38.670 [main] DEBUG org.apache.commons.beanutils.converters.IntegerConverter -     No conversion required, value is already a Integer
    17:37:38.670 [main] DEBUG org.apache.commons.beanutils.converters.LongConverter - Setting default value: 0
    17:37:38.670 [main] DEBUG org.apache.commons.beanutils.converters.LongConverter - Converting 'Integer' value '0' to type 'Long'
    17:37:38.670 [main] DEBUG org.apache.commons.beanutils.converters.LongConverter -     Converted to Long value '0'
    17:37:38.671 [main] DEBUG org.apache.commons.beanutils.converters.ShortConverter - Setting default value: 0
    17:37:38.671 [main] DEBUG org.apache.commons.beanutils.converters.ShortConverter - Converting 'Integer' value '0' to type 'Short'
    17:37:38.671 [main] DEBUG org.apache.commons.beanutils.converters.ShortConverter -     Converted to Short value '0'
    17:37:38.673 [main] DEBUG org.apache.commons.beanutils.converters.BigDecimalConverter - Setting default value: 0.0
    17:37:38.673 [main] DEBUG org.apache.commons.beanutils.converters.BigDecimalConverter - Converting 'BigDecimal' value '0.0' to type 'BigDecimal'
    17:37:38.673 [main] DEBUG org.apache.commons.beanutils.converters.BigDecimalConverter -     No conversion required, value is already a BigDecimal
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.BigIntegerConverter - Setting default value: 0
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.BigIntegerConverter - Converting 'BigInteger' value '0' to type 'BigInteger'
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.BigIntegerConverter -     No conversion required, value is already a BigInteger
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.BooleanConverter - Setting default value: false
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.BooleanConverter - Converting 'Boolean' value 'false' to type 'Boolean'
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.BooleanConverter -     No conversion required, value is already a Boolean
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.ByteConverter - Setting default value: 0
    17:37:38.674 [main] DEBUG org.apache.commons.beanutils.converters.ByteConverter - Converting 'Integer' value '0' to type 'Byte'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.ByteConverter -     Converted to Byte value '0'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.CharacterConverter - Setting default value:  
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.CharacterConverter - Converting 'Character' value ' ' to type 'Character'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.CharacterConverter -     No conversion required, value is already a Character
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.DoubleConverter - Setting default value: 0
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.DoubleConverter - Converting 'Integer' value '0' to type 'Double'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.DoubleConverter -     Converted to Double value '0.0'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.FloatConverter - Setting default value: 0
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.FloatConverter - Converting 'Integer' value '0' to type 'Float'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.FloatConverter -     Converted to Float value '0.0'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.IntegerConverter - Setting default value: 0
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.IntegerConverter - Converting 'Integer' value '0' to type 'Integer'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.IntegerConverter -     No conversion required, value is already a Integer
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.LongConverter - Setting default value: 0
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.LongConverter - Converting 'Integer' value '0' to type 'Long'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.LongConverter -     Converted to Long value '0'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.ShortConverter - Setting default value: 0
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.ShortConverter - Converting 'Integer' value '0' to type 'Short'
    17:37:38.675 [main] DEBUG org.apache.commons.beanutils.converters.ShortConverter -     Converted to Short value '0'
    17:37:38.676 [main] DEBUG org.apache.commons.beanutils.converters.StringConverter - Setting default value: 
    17:37:38.676 [main] DEBUG org.apache.commons.beanutils.converters.StringConverter - Converting 'String' value '' to type 'String'
    17:37:38.683 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Z@5679c6c6
    17:37:38.683 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'boolean[]' value '[Z@5679c6c6' to type 'boolean[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a boolean[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [B@19e1023e
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'byte[]' value '[B@19e1023e' to type 'byte[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a byte[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [C@64b8f8f4
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'char[]' value '[C@64b8f8f4' to type 'char[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a char[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [D@3cd1f1c8
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'double[]' value '[D@3cd1f1c8' to type 'double[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a double[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [F@1996cd68
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'float[]' value '[F@1996cd68' to type 'float[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a float[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [I@555590
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'int[]' value '[I@555590' to type 'int[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a int[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [J@424c0bc4
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'long[]' value '[J@424c0bc4' to type 'long[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a long[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [S@16b4a017
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'short[]' value '[S@16b4a017' to type 'short[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a short[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.math.BigDecimal;@2a3046da
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'BigDecimal[]' value '[Ljava.math.BigDecimal;@2a3046da' to type 'BigDecimal[]'
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a BigDecimal[]
    17:37:38.684 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.math.BigInteger;@198e2867
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'BigInteger[]' value '[Ljava.math.BigInteger;@198e2867' to type 'BigInteger[]'
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a BigInteger[]
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Boolean;@3ada9e37
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Boolean[]' value '[Ljava.lang.Boolean;@3ada9e37' to type 'Boolean[]'
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Boolean[]
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Byte;@3419866c
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Byte[]' value '[Ljava.lang.Byte;@3419866c' to type 'Byte[]'
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Byte[]
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Character;@68fb2c38
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Character[]' value '[Ljava.lang.Character;@68fb2c38' to type 'Character[]'
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Character[]
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Double;@2eafffde
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Double[]' value '[Ljava.lang.Double;@2eafffde' to type 'Double[]'
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Double[]
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Float;@6842775d
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Float[]' value '[Ljava.lang.Float;@6842775d' to type 'Float[]'
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Float[]
    17:37:38.685 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Integer;@64cee07
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Integer[]' value '[Ljava.lang.Integer;@64cee07' to type 'Integer[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Integer[]
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Long;@6c629d6e
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Long[]' value '[Ljava.lang.Long;@6c629d6e' to type 'Long[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Long[]
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Short;@3f102e87
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Short[]' value '[Ljava.lang.Short;@3f102e87' to type 'Short[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Short[]
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.String;@5f5a92bb
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'String[]' value '[Ljava.lang.String;@5f5a92bb' to type 'String[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a String[]
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.lang.Class;@51016012
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Class[]' value '[Ljava.lang.Class;@51016012' to type 'Class[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Class[]
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.util.Date;@2280cdac
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Date[]' value '[Ljava.util.Date;@2280cdac' to type 'Date[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Date[]
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.util.Calendar;@4fccd51b
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'Calendar[]' value '[Ljava.util.Calendar;@4fccd51b' to type 'Calendar[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a Calendar[]
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.io.File;@60215eee
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'java.io.File[]' value '[Ljava.io.File;@60215eee' to type 'java.io.File[]'
    17:37:38.686 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a java.io.File[]
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.sql.Date;@65e579dc
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'java.sql.Date[]' value '[Ljava.sql.Date;@65e579dc' to type 'java.sql.Date[]'
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a java.sql.Date[]
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.sql.Time;@b065c63
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'java.sql.Time[]' value '[Ljava.sql.Time;@b065c63' to type 'java.sql.Time[]'
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a java.sql.Time[]
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.sql.Timestamp;@490d6c15
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'java.sql.Timestamp[]' value '[Ljava.sql.Timestamp;@490d6c15' to type 'java.sql.Timestamp[]'
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a java.sql.Timestamp[]
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Setting default value: [Ljava.net.URL;@449b2d27
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter - Converting 'java.net.URL[]' value '[Ljava.net.URL;@449b2d27' to type 'java.net.URL[]'
    17:37:38.687 [main] DEBUG org.apache.commons.beanutils.converters.ArrayConverter -     No conversion required, value is already a java.net.URL[]
    17:37:38.706 [main] DEBUG org.apache.shiro.config.IniFactorySupport - Creating instance from Ini [sections=users,roles]
    17:37:38.736 [main] DEBUG org.apache.shiro.realm.text.IniRealm - Discovered the [roles] section.  Processing...
    17:37:38.739 [main] DEBUG org.apache.shiro.realm.text.IniRealm - Discovered the [users] section.  Processing...
    17:37:38.754 [main] DEBUG org.apache.shiro.session.mgt.AbstractValidatingSessionManager - No sessionValidationScheduler set.  Attempting to create default instance.
    17:37:38.755 [main] INFO org.apache.shiro.session.mgt.AbstractValidatingSessionManager - Enabling session validation scheduler...
    17:37:38.761 [main] DEBUG org.apache.shiro.session.mgt.DefaultSessionManager - Creating new EIS record for new session instance [org.apache.shiro.session.mgt.SimpleSession,id=null]
    17:37:39.125 [main] INFO com.ryan.shiro.sample.QuickStart - 存储当前的值! [aValue]
    17:37:39.125 [main] DEBUG org.apache.shiro.realm.AuthenticatingRealm - Looked up AuthenticationInfo [lonestarr] from doGetAuthenticationInfo
    17:37:39.125 [main] DEBUG org.apache.shiro.realm.AuthenticatingRealm - AuthenticationInfo caching is disabled for info [lonestarr].  Submitted token: [org.apache.shiro.authc.UsernamePasswordToken - lonestarr, rememberMe=true].
    17:37:39.125 [main] DEBUG org.apache.shiro.authc.credential.SimpleCredentialsMatcher - Performing credentials equality check for tokenCredentials of type [[C and accountCredentials of type [java.lang.String]
    17:37:39.125 [main] DEBUG org.apache.shiro.authc.credential.SimpleCredentialsMatcher - Both credentials arguments can be easily converted to byte arrays.  Performing array equals comparison
    17:37:39.126 [main] DEBUG org.apache.shiro.authc.AbstractAuthenticator - Authentication successful for token [org.apache.shiro.authc.UsernamePasswordToken - lonestarr, rememberMe=true].  Returned account [lonestarr]
    17:37:39.126 [main] DEBUG org.apache.shiro.mgt.DefaultSecurityManager - Context already contains a session.  Returning.
    17:37:39.127 [main] INFO com.ryan.shiro.sample.QuickStart - User [lonestarr] logged in successfully.
    17:37:39.127 [main] INFO com.ryan.shiro.sample.QuickStart - May the Schwartz be with you!
    17:37:39.127 [main] INFO com.ryan.shiro.sample.QuickStart - You may use a lightsaber ring.  Use it wisely.
    17:37:39.127 [main] INFO com.ryan.shiro.sample.QuickStart - You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  Here are the keys - have fun!
    17:37:39.127 [main] DEBUG org.apache.shiro.mgt.DefaultSecurityManager - Logging out subject with primary principal lonestarr
    17:37:39.128 [main] DEBUG org.apache.shiro.session.mgt.AbstractSessionManager - Stopping session with id [817743ff-8d08-481c-bdf0-3fbb16b6042c]
    
    Process finished with exit code 0

    ```
