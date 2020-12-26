package com.bowoon.android.paging_example.viewmodels

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bowoon.android.paging_example.model.PersonModel
import com.bowoon.android.paging_example.network.api.PersonApi
import com.bowoon.android.paging_example.utils.PaginationStatus
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class PersonViewModelTest {
    companion object {
        const val TAG = "PersonViewModelTest"
    }
//    @Mock
    val viewModel = PersonViewModel()
    @Mock
    val liveData = MutableLiveData<PaginationStatus>()
    @Mock
    val observer = Observer<PaginationStatus> {
        if (it == PaginationStatus.Loading) {
            Log.d("PersonViewModelTest", it.toString())
        }
    }

    lateinit var response: MockResponse
    var mockWebServer: MockWebServer = MockWebServer()
    lateinit var api: PersonApi
    val value = "{\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"gender\": \"male\",\n" +
            "      \"name\": {\n" +
            "        \"title\": \"Mr\",\n" +
            "        \"first\": \"Onni\",\n" +
            "        \"last\": \"Maijala\"\n" +
            "      },\n" +
            "      \"location\": {\n" +
            "        \"street\": {\n" +
            "          \"number\": 6246,\n" +
            "          \"name\": \"Tahmelantie\"\n" +
            "        },\n" +
            "        \"city\": \"Tammela\",\n" +
            "        \"state\": \"Finland Proper\",\n" +
            "        \"country\": \"Finland\",\n" +
            "        \"postcode\": 83360,\n" +
            "        \"coordinates\": {\n" +
            "          \"latitude\": \"-10.9673\",\n" +
            "          \"longitude\": \"-14.7862\"\n" +
            "        },\n" +
            "        \"timezone\": {\n" +
            "          \"offset\": \"-7:00\",\n" +
            "          \"description\": \"Mountain Time (US & Canada)\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"email\": \"onni.maijala@example.com\",\n" +
            "      \"login\": {\n" +
            "        \"uuid\": \"b829ed2d-76a8-4098-ae00-c458ac900505\",\n" +
            "        \"username\": \"angrybear815\",\n" +
            "        \"password\": \"unreal\",\n" +
            "        \"salt\": \"DyNFcJFO\",\n" +
            "        \"md5\": \"1a426ca881974d825d0eead482f5d3fb\",\n" +
            "        \"sha1\": \"35c765566611f621f63bc320ac3bacf5f22543c5\",\n" +
            "        \"sha256\": \"ea0dd17c08b648241a34d03a20c1a4ebf5806f267c8f7aec8369ebd5f3147d5d\"\n" +
            "      },\n" +
            "      \"dob\": {\n" +
            "        \"date\": \"1959-09-25T16:25:16.399Z\",\n" +
            "        \"age\": 61\n" +
            "      },\n" +
            "      \"registered\": {\n" +
            "        \"date\": \"2008-07-04T10:27:58.027Z\",\n" +
            "        \"age\": 12\n" +
            "      },\n" +
            "      \"phone\": \"07-760-030\",\n" +
            "      \"cell\": \"043-507-46-13\",\n" +
            "      \"id\": {\n" +
            "        \"name\": \"HETU\",\n" +
            "        \"value\": \"NaNNA845undefined\"\n" +
            "      },\n" +
            "      \"picture\": {\n" +
            "        \"large\": \"https://randomuser.me/api/portraits/men/54.jpg\",\n" +
            "        \"medium\": \"https://randomuser.me/api/portraits/med/men/54.jpg\",\n" +
            "        \"thumbnail\": \"https://randomuser.me/api/portraits/thumb/men/54.jpg\"\n" +
            "      },\n" +
            "      \"nat\": \"FI\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"info\": {\n" +
            "    \"seed\": \"8a3ce16b27b91dc4\",\n" +
            "    \"results\": 1,\n" +
            "    \"page\": 1,\n" +
            "    \"version\": \"1.3\"\n" +
            "  }\n" +
            "}"

    @Before
    fun setUpRxSchedulers() {
        println("setUpRxSchedulers")
        val immediate: Scheduler = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, delay, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorWorker(Executor { obj: Runnable -> obj.run() }, true)
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    @Before
    fun setUp() {
//        MockitoAnnotations.openMocks(this)
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                println(request.path)
                return when (request.path) {
                    "/api/?page=1&results=1&gender=" -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(value)
                    }
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        response = MockResponse()
            .setResponseCode(200)
            .setBody(value)
        mockWebServer.start()

        api = Retrofit.Builder().apply {
//            client(createOkHttpClient())
            baseUrl(mockWebServer.url("/"))
            addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaTypeOrNull()!!))
            addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        }.build().create(PersonApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun test() {
        val result = api.getUsers(1, 1, "").blockingGet()
        val model = Json { ignoreUnknownKeys = true }.decodeFromString<PersonModel>(value)
        assertNotNull(result)
        assertNotNull(model)
        assertEquals(result, model)
        println(result)
        println(model)
    }

    @Test
    fun getCompositeDisposable() {
    }

    @Test
    fun add() {
//        val viewModel = mock(PersonViewModel::class.java)
//        doNothing().`when`(viewModel).add("male")
//        doNothing().`when`(viewModel).add("female")
//        `when`(viewModel.getPersonData("male")).thenReturn(Flowable.create({}, BackpressureStrategy.BUFFER))

        viewModel.add("male")
        viewModel.add("female")
        viewModel.getPersonData("male")

//        verify(viewModel, times(2)).add(anyString())
//        verify(viewModel, times(1)).getPersonData("male")
//        verify(viewModel).add("male")
//        verify(viewModel).add("female")
//        verify(viewModel).getPersonData("male")
        assertNotNull(viewModel.getPersonData("male"))
        assertNotNull(viewModel.getPersonData("female"))
        assertNull(viewModel.getPersonData("Hello"))
    }

    @Test
    fun getPersonData() {
    }

//    @Test
//    fun getPaginationState() {
//        `when`(viewModel.getPaginationState()).thenReturn(liveData)
//
//        viewModel.paginationStatus.observeForever(observer)
//        viewModel.getPaginationState()
//        observer.onChanged(PaginationStatus.Loading)
//        observer.onChanged(PaginationStatus.NotEmpty)
//
//        verify(viewModel, times(1)).getPaginationState()
//        verify(observer).onChanged(PaginationStatus.Loading)
//        verify(observer).onChanged(PaginationStatus.NotEmpty)
//        assertEquals(liveData, viewModel.getPaginationState())
//    }

    @Test
    fun onCleared() {
    }
}