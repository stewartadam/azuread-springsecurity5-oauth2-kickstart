AzureAD, Spring Security 5, and OAuth 2 Login Kickstart
======
A minimal Spring Boot 2.0 project demonstrating how to integrate Azure AD with the new Spring Security 5 + OAuth 2 login features.

## Why?
Because Spring Security recently moved some of the functionality from the previously recommended `spring-security-oauth` package into core, and will replace it entirely over time. See [this compatibility matrix](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix) for details. Many examples on the web are no longer compatible with Spring Boot 2.0 / Spring Security 5.0 after this breaking change.

## How does it work?
1. Edit `src/main/resources/application.yaml` and plug in your Azure AD application's client ID and secret. The app needs to be registered in Azure AD with at least the "Sign-in and read user profile" permission (i.e. User.Read scope) against *Microsoft Graph* (not the default Windows Azure AD Graph API that is added for new apps).
2. Run `mvn spring-boot:run` to start the local webserver.
3. Open one of the endpoints below in your browser of choice

## Available Endpoints
1. `http://localhost:8080/hello/foo`: echoes `foo` back at you (for your choice of `foo`)
2. `http://localhost:8080/oauth2/authorization/microsoft`: attempts to login the user using the OAuth2 code grant against AAD, followed by a Graph call to obtain user information 
3. `http://localhost:8080/claims`: Displays info on the currently logged in user

Note that because no homepage is configured, after login you'll get a 404 error when hitting `http://localhost:8080`. This is expected; hit the `/claims` endpoint to verify that your session has the claims stored.

## References:
I found these resources helpful while troubleshooting:

-   [Spring Security's OAuth2 feature matrix](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix)
-   [Spring Security 5 OAuth 2.0 Login Sample (Okta)](https://github.com/spring-projects/spring-security/tree/master/samples/boot/oauth2login#okta-login)
-   [Spring Security 5 -- OAuth2 Login](http:/www.baeldung.com/spring-security-5-oauth2-login) by Loredana Crusoveanu
-   [Overriding Spring Boot 2.0 Auto-configuration](https:/docs.spring.io/spring-security/site/docs/5.0.3.RELEASE/reference/htmlsingle#jc-oauth2login-completely-override-autoconfiguration) from Spring docs
-   (Deprecated) [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/) tutorial
-   [Spring Boot's OAuth2 Client](https:/docs.spring.io/spring-boot/docs/current/reference/html/boot-features-security.html#boot-features-security-oauth2-client) from Spring docs
-   ['cannot be null' errors in Spring Security 5](https://stackoverflow.com/questions/49315552/authorizationgranttype-cannot-be-null-in-spring-security-5-oauth-client-and-spri)

A lesson learned: searching specifically for Spring Security 5's OAuth2 implementation is near impossible since you always end up getting results for the older "Spring Security OAuth" 2.x implementation, even if you put a time restriction on results such as "within last year".

Based on lots of trial and error, I would caution that you are **not** reading about the new Spring Security 5's OAuth2 implementation if you see:
1. `@EnableOAuth2Sso` in the code sample
2. The properties/YAML configuration start directly with `security` (instead of `spring` followed by `security`, which indicates for Spring Security 5)
3. The properties/YAML configuration in [camelCase](https://github.com/spring-guides/tut-spring-boot-oauth2/blob/master/auth-server/src/main/resources/application.yml) (new config in Spring Security 5 is [snake-case](https://github.com/spring-projects/spring-security/blob/master/samples/boot/oauth2login/src/main/resources/application.yml))
4. A dependency on the `spring-security-oauth2` package in `pom.xml` (but `spring-security-oauth2-client` or `spring-security-oauth2-jose` would be OK for Spring Security 5)

*Disclaimer: I am not a Spring framework expert, this is my best guess from limited experience with it*

## Known issues
Java 8 (and possibly later) [default to an invalid Accept header value](https://bugs.openjdk.java.net/browse/JDK-8163921) of `text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2` which upsets many API endpoints, including Microsoft Graph (besides the point that a REST endpoint is not going to be able to spit back user info in HTML/GIF/JPEG format to Java).

As a result, two classes from Spring needed to be customized (e.g. copied and lightly modified) in order to ensure the appropriate Accept header was used. You will find these two classes in the `replacements` sub-package.
