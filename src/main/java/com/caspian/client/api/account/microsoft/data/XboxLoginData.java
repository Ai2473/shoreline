/* November.lol © 2023 */
package com.caspian.client.api.account.microsoft.data;

/**
 * @param token the xbox live token
 * @param uhs   the UHS data (honestly no fucking clue what this is)
 * @author Gavin
 * @since 2.0.0
 */
public record XboxLoginData(String token, String uhs) {}
