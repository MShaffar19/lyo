/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *  
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *  
 *  Contributors:
 *  
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.lyo.server.oauth.core.token;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;

import org.eclipse.lyo.server.oauth.core.OAuthRequest;

/**
 * Manages and validates OAuth tokens and token secrets.
 * {@link SimpleTokenStrategy} is a basic implementation, but you can implement
 * this interface to generate and validate OAuth tokens your own way.
 * 
 * @author Samuel Padgett <spadgett@us.ibm.com>
 */
public interface TokenStrategy {
	/**
	 * Generates a request token and token secret and sets it in the accessor in
	 * the {@link OAuthRequest}.
	 * 
	 * @param oAuthRequest
	 *            the OAuth request
	 * @see OAuthRequest#getAccessor()
	 */
	public void generateRequestToken(OAuthRequest oAuthRequest);

	/**
	 * Validates that the request token is valid, throwing an exception if not.
	 * Returns the consumer key so that the authorization page can display
	 * information about the consumer. The token strategy must track what
	 * request tokens belong to what consumers since the consumer key is not
	 * guaranteed to be in the request.
	 * 
	 * @param httpRequest
	 *            the HTTP request
	 * @param message
	 *            the OAuth message
	 * 
	 * @return the consumer key associated with the request
	 * 
	 * @throws OAuthException
	 *             if the tokens are not valid
	 * @throws IOException
	 *             on I/O errors
	 */
	public String validateRequestToken(HttpServletRequest httpRequest,
			OAuthMessage message) throws OAuthException, IOException;

	/**
	 * Indicates that a user has typed in a valid ID and password, and that the
	 * request token can now be exchanged for an access token.
	 * 
	 * @param httpRequest
	 *            the servlet request
	 * @param requestToken
	 *            the request token string
	 * @throws OAuthProblemException
	 *             if the token is not valid
	 *             
	 * @see #isRequestTokenAuthorized(HttpServletRequest, String)
	 */
	public void markRequestTokenAuthorized(HttpServletRequest httpRequest,
			String requestToken) throws OAuthProblemException;

	/**
	 * Checks with the request token has been authorized by the end user.
	 * 
	 * @param httpRequest
	 *            the servlet request
	 * @param requestToken
	 *            the request token
	 * @return answers if the request token is authorized and can be exchanged
	 *         for an access token
	 * 
	 * @see #markRequestTokenAuthorized(HttpServletRequest, OAuthAccessor)
	 */
	public boolean isRequestTokenAuthorized(HttpServletRequest httpRequest,
			String requestToken);

	/**
	 * Generates an access token and token secret and sets it in the accessor in
	 * the {@link OAuthRequest}. Clears any request tokens set.
	 * 
	 * @param oAuthRequest
	 *            the OAuth request
	 * @throws OAuthProblemException
	 *             on OAuth problems
	 * @throws IOException
	 *             on I/O errors
	 * @see OAuthRequest#getAccessor()
	 */
	public void generateAccessToken(OAuthRequest oAuthRequest)
			throws OAuthProblemException, IOException;

	/**
	 * Validates that the access token is valid, throwing an exception if not.
	 * 
	 * @param oAuthRequest
	 *            the OAuth request
	 * @throws OAuthException
	 *             if the token is invalid
	 * @throws IOException
	 *             on I/O errors
	 */
	public void validateAccessToken(OAuthRequest oAuthRequest)
			throws OAuthException, IOException;

	/**
	 * Gets the token secret for token to validate signatures.
	 * 
	 * @param httpRequest
	 *            the HTTP request
	 * @param token
	 *            the token string, either a request token or access token
	 * 
	 * @return the token secret
	 * @throws OAuthProblemException
	 *             on OAuth problems (e.g., the token is invalid)
	 */
	public String getTokenSecret(HttpServletRequest httpRequest, String token)
			throws OAuthProblemException;
}