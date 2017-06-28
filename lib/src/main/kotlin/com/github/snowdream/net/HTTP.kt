package com.github.snowdream.net

/**
 * see: http://grepcode.com/file_/repo1.maven.org/maven2/org.apache.httpcomponents/httpcore/4.4.1/org/apache/http/protocol/HTTP.java/?v=source
 *
 * Created by snowdream on 17/6/28.
 */
class HTTP private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        @JvmField val CR = 13 // <US-ASCII CR, carriage return (13)>
        @JvmField val LF = 10 // <US-ASCII LF, linefeed (10)>
        @JvmField val SP = 32 // <US-ASCII SP, space (32)>
        @JvmField val HT = 9  // <US-ASCII HT, horizontal-tab (9)>

        /**
         * HTTP header definitions
         */
        @JvmField val TRANSFER_ENCODING = "Transfer-Encoding"
        @JvmField val CONTENT_LEN = "Content-Length"
        @JvmField val CONTENT_TYPE = "Content-Type"
        @JvmField val CONTENT_ENCODING = "Content-Encoding"
        @JvmField val EXPECT_DIRECTIVE = "Expect"
        @JvmField val CONN_DIRECTIVE = "Connection"
        @JvmField val TARGET_HOST = "Host"
        @JvmField val USER_AGENT = "User-Agent"
        @JvmField val DATE_HEADER = "Date"
        @JvmField val SERVER_HEADER = "Server"

        /**
         *  Default User-Agent
         */
        @JvmField val DEFAULT_USER_AGENT = System.getProperty("http.agent")

        /**
         * HTTP expectations
         */
        @JvmField val EXPECT_CONTINUE = "100-continue"

        /**
         * HTTP connection control
         */
        @JvmField val CONN_CLOSE = "Close"
        @JvmField val CONN_KEEP_ALIVE = "Keep-Alive"

        /**
         * Transfer encoding definitions
         */
        @JvmField val CHUNK_CODING = "chunked"
        @JvmField val IDENTITY_CODING = "identity"

        /**
         * Common charset definitions
         */
        @JvmField val UTF_8 = "UTF-8"
        @JvmField val UTF_16 = "UTF-16"
        @JvmField val US_ASCII = "US-ASCII"
        @JvmField val ASCII = "ASCII"
        @JvmField val ISO_8859_1 = "ISO-8859-1"

        /**
         * Default charsets
         */
        @JvmField val DEFAULT_CONTENT_CHARSET = ISO_8859_1
        @JvmField val DEFAULT_PROTOCOL_CHARSET = US_ASCII

        /**
         * Content type definitions
         */
        @JvmField val OCTET_STREAM_TYPE = "application/octet-stream"
        @JvmField val PLAIN_TEXT_TYPE = "text/plain"
        @JvmField val CHARSET_PARAM = "; charset="

        /**
         * Default content type
         */
        @JvmField val DEFAULT_CONTENT_TYPE = OCTET_STREAM_TYPE

        fun isWhitespace(ch: Char): Boolean {
            return ch.toInt() == SP || ch.toInt() == HT || ch.toInt() == CR || ch.toInt() == LF
        }
    }
}