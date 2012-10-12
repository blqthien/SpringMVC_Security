# Spring Security

This is a simple example on how to create a simple Spring MVC application secured by Spring Security. The example is based on the SpringMVC template provided by the Spring Tool Suite (STS). The basic MVC setup are not described.

## Configuration

### Minimum configuration

#### Libraries

Add the Spring Security Libraries to your application by adding the following lines to your `pom.xml`

    <!-- Spring Security -->
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-core</artifactId>
		<version>${org.springframework-version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-web</artifactId>
		<version>${org.springframework-version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-config</artifactId>
		<version>${org.springframework-version}</version>
	</dependency>

#### Security Filter

To enable Spring Security in your web application you need to add the Spring Security filter chain to your web.xml


	<!-- Add Spring Security Filter Chain-->
	<filter>
		<filter-name>springSecurityFilterChain</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>springSecurityFilterChain</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	
The security filter expects a bean named springSecurityFilterChain. This bean is automatically created when adding the security namespace to your spring configuration. Spring security configuration is usually added in a separate `security-context.xml` configuration file.

Update `web.xml` to add the config file
    
    <context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>
			/WEB-INF/spring/root-context.xml
			/WEB-INF/spring/security-context.xml
		</param-value>
	</context-param>
	
Create `security-context.xml`

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans" 
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 xmlns:security="http://www.springframework.org/schema/security"
	 xsi:schemaLocation="http://www.springframework.org/schema/security 
	 http://www.springframework.org/schema/security/spring-security-3.1.xsd
	 http://www.springframework.org/schema/beans 
	 http://www.springframework.org/schema/beans/spring-beans.xsd">

	<security:http auto-config='true'>
		<security:intercept-url pattern="/**" access="ROLE_USER" />
	</security:http>

	<security:authentication-manager>
		<security:authentication-provider>
			<security:user-service>
				<security:user name="user" password="user" authorities="ROLE_USER" />
			</security:user-service>
		</security:authentication-provider>
	</security:authentication-manager>
	</beans>

### Adding support for LDAP

Adding support for authentication and authorization using LDAP is quite simple. Just add another authenitcation provider to your authentication manager configuration.

	<security:ldap-authentication-provider/>
	
The default settings will look for a ldap-server bean with ldap server configuration.
Spring supports using an embedded Apache Directory server. This is easily configured by adding

	<security:ldap-server ldif="classpath:inmemoryldap.ldif"/>

to your security-context.

Even though the configuration is quite easy it requires quite a lot of new libraries. THe following section extra depenencies are required.

		<!-- LDAP  -->
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-ldap</artifactId>
			<version>${org.springframework-version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.ldap</groupId>
			<artifactId>spring-ldap-core</artifactId>
			<version>1.3.1.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.apache.directory.server</groupId>
			<artifactId>apacheds-all</artifactId>
			<version>1.5.5</version>
		</dependency>

Note: It seems like the latest version of Apache Directory server (1.5.7) is not compatible woth Spring 3.1.0 and Spring LDAP 1.3.1
		
For more details on LDAP configuration see the [Spring Security Ldap documentation](http://static.springsource.org/spring-security/site/docs/3.1.x/reference/springsecurity-single.html#ldap-server)
		
To visualize successful authentication and authorization the [Spring Security taglib](http://static.springsource.org/spring-security/site/docs/3.1.x/reference/springsecurity-single.html#taglibs) was added as well

	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-taglibs</artifactId>
		<version>${org.springframework-version}</version>
	</dependency>		

See `home.jsp`for example usage.

	
## Test

### Browser testing

Currently only simple url based security is implemented. To verify the setup a few simple Selenium WebDriver tests have been configured in `VerifySecuritySetup.java`

The tests require the application to be running. Currently this is hardcoded to `http://localhost:8080/SpringMVCWithSecurity`. Because of this the test is also exclude from the Maven test phase (by naming convention).

### Integration testing

To support testing of functionality that requires the spring context you should utilize the helper classes provided in the spring-test module. Add this dependency to the pom.

	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-test</artifactId>
		<version>${org.springframework-version}</version>
	</dependency>

##### Authentication managers

To test the authentication managers a simple integration test developed `TestAuthenticationManagers.java`. The test are excluded in `infinitest.filters` as it loads the security-context and starts an in memory LDAP instance which makes it slow.