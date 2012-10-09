# Spring Security

This is a simple example on how to create a simple Spring MVC application secured by Spring Security. The example is based on the SpringMVC template provided by the Spring Tool Suite (STS). The basic MVC setup are not described.

## Configuration

### Libraries

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

### Security Filter

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

## Integration Test

Currently only simple url based security is implemented. To verify the setup a few simple Selenium WebDriver tests have been configured in `VerifySecuritySetup.java`

The tests require the application to be running. Currently this is hardcoded to `http://localhost:8080/SpringMVCWithSecurity`. Because of this the test is also exclude from the Maven test phase (by naming convention).