package no.iterate.integration.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/security-context.xml"})
public class TestAuthenticationManagers {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Test
	public void authenticateUsingValidUserFromInMemoryAuthenticationManager(){
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user", "user"));
		assertTrue("User is authenticated", authenticate.isAuthenticated());
	}
	
	@Test
	public void authorizeUsingValidUserFromInMemoryAuthenticationManager(){
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user", "user"));
		assertEquals("A user should have exact one role",1,authenticate.getAuthorities().size());
		assertEquals("A user should have the role ROLE_USER","ROLE_USER",authenticate.getAuthorities().iterator().next().getAuthority());
	}
	
	@Test
	public void authenticateUsingValidUserFromLdapAuthenticationManager(){
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("stein", "Test123"));
		assertTrue("User is authenticated", authenticate.isAuthenticated());
	}
	
	@Test
	public void authorizeUsingValidUserFromLdapAuthenticationManager(){
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("stein", "Test123"));
		assertEquals("A user should have exact one role",1,authenticate.getAuthorities().size());
		assertEquals("A user should have the role ROLE_USER","ROLE_USER",authenticate.getAuthorities().iterator().next().getAuthority());
	}
	
	@Test(expected=BadCredentialsException.class)
	public void authenticateUsingInvalidUser(){
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("invalid", "user"));
	}
	
}
