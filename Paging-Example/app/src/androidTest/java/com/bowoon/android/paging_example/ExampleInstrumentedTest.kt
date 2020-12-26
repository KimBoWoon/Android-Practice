//package com.bowoon.android.paging_example
//
//import android.util.Log
//import androidx.test.espresso.IdlingRegistry
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.platform.app.InstrumentationRegistry
//import com.bowoon.android.paging_example.activities.MainActivity
//import com.bowoon.android.paging_example.network.api.PersonApi
//import com.bowoon.android.paging_example.network.provider.createOkHttpClient
//import com.bowoon.android.paging_example.network.provider.providePersonApi
//import com.jakewharton.espresso.OkHttp3IdlingResource
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import kotlinx.serialization.json.Json
//import okhttp3.HttpUrl
//import okhttp3.HttpUrl.Companion.toHttpUrl
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.OkHttpClient
//import okhttp3.mockwebserver.Dispatcher
//import okhttp3.mockwebserver.MockResponse
//import okhttp3.mockwebserver.MockWebServer
//import okhttp3.mockwebserver.RecordedRequest
//import org.junit.After
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    companion object {
//        const val TAG = "ExampleInstrumentedTest"
//    }
////    @get:Rule
////    val activityRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)
//    var mockWebServer: MockWebServer = MockWebServer()
//    lateinit var api: PersonApi
//
//    @Before
//    fun setUp() {
//        mockWebServer.start()
////        mockWebServer.dispatcher = object : Dispatcher() {
////            override fun dispatch(request: RecordedRequest): MockResponse {
////                return MockResponse()
////                    .setResponseCode(200)
////                    .setBody(FileReader.readStringFromFile("success_response.json"))
////            }
////        }
//
//        api = Retrofit.Builder().apply {
//            baseUrl(mockWebServer.url("https://randomuser.me/"))
//            client(createOkHttpClient())
//            addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaTypeOrNull()!!))
//            addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
//        }.build().create(PersonApi::class.java)
//    }
//
//    @After
//    fun tearDown() {
//        mockWebServer.shutdown()
//    }
//
//    @Test
//    fun test() {
//        val response = MockResponse()
//            .setResponseCode(200)
//            .setBody(FileReader.readStringFromFile("success_response.json"))
//        mockWebServer.enqueue(response)
//        val result = api.getUsers(1, 1, "").blockingGet()
//        Log.d(TAG, result.toString())
//    }
//
//    @Test
//    fun getCompositeDisposable() {
//        "https://randomuser.me/".toHttpUrl()
//        mockWebServer.enqueue(MockResponse().setBody(""))
//
//        mockWebServer.start()
//        val url = mockWebServer.url("/api/?results=10&page=1")
//        providePersonApi()
//            .getUsers(1, 10, "")
////        mockWebServer.url("https://randomuser.me/api/me?results=10&page=1")
//        Log.d(TAG, mockWebServer.takeRequest().toString())
//    }
//
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.bowoon.android.paging_example", appContext.packageName)
//    }
//}
//
//class OurApi(
//    private val baseUrl: String = "https://randomuser.me/"
//) {
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(baseUrl)
////        .addConverterFactory(MoshiConverterFactory.create())
//        .build()
//}