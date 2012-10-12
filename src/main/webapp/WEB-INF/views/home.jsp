<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<!-- User display -->
<h1>
	Hello <sec:authentication property="principal.username" />!
</h1>

<!-- Roles display -->
<sec:authentication property="authorities" var="roles" scope="page" />
Your current roles are:
<ul>
    <c:forEach var="role" items="${roles}">
    <li>${role}</li>
    </c:forEach>
</ul>

<P>  The time on the server is ${serverTime}. </P>
</body>
</html>
