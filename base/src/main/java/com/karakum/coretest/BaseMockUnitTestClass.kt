package com.karakum.coretest

import androidx.annotation.CallSuper
import com.karakum.IObservableSchedulerRx2
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.io.ByteArrayOutputStream

/**
 * This is a BASE test class which should be extended by any base unit test class in order handle api stuff.
 * This class also contains measures for converting a local resource file into a mock 'response'
 */
abstract class BaseMockUnitTestClass {

    protected val scheduler: IObservableSchedulerRx2 = IObservableSchedulerRx2.TRAMPOLINE
    protected lateinit var mockWebServer: MockWebServer

    open val okHttpClient by lazy(LazyThreadSafetyMode.NONE) {
        OkHttpClient()
    }

    @Before
    @CallSuper
    @Throws(Exception::class)
    open fun setUp() {
        //Instantiate and start the MockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.url("/")
    }

    @After
    @CallSuper
    @Throws(Exception::class)
    open fun tearDown() {
        //Shut down the MockWebServer
        mockWebServer.shutdown()
    }

    /**
     * Gets a string for a resource file
     */
    protected fun getResource(resourceFile: String): String {
        return ApiUtils.convertResourceToString(javaClass, resourceFile)
    }

    /**
     * Method to get a mock response
     */
    protected fun response(resourceFile: String, responseCode: Int): MockResponse {
        return MockResponse()
                .setBody(getResource(resourceFile))
                .setResponseCode(responseCode)
    }


    /**
     * Method to get a mock response without a repsonse code
     */
    protected fun response(resourceFile: String): MockResponse {
        return response(resourceFile, 200)
    }


    internal class ApiUtils {

        companion object {

            /**
             * Takes a file in the resources folder of calling module and returns it as a String
             */
            fun convertResourceToString(clzz: Class<Any>, fileName: String): String {
                return try {
                    //Get input byte stream representing the file, java does it's delegation hierarchy model shit
                    //https://www.geeksforgeeks.org/classloader-in-java/
                    val inputStream = clzz.classLoader?.getResourceAsStream(fileName)
                    val result = ByteArrayOutputStream()
                    //While we a re still reading bytes from file, write those bytes t
                    val buffer = inputStream?.readBytes()
                    result.write(buffer!!, 0, buffer.size)
                    inputStream.close()
                    result.toString("UTF-8")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    ""
                }
            }

        }
    }


}