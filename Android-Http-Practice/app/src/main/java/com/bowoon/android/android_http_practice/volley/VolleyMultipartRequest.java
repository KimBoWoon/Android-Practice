package com.bowoon.android.android_http_practice.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bowoon.android.android_http_practice.file.FileDataPart;
import com.bowoon.android.android_http_practice.option.HttpOption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

// MultiPartRequest를 하기위해 따로 커스텀
public class VolleyMultipartRequest extends Request<NetworkResponse> {
    private final String TWO_HYPHENS = "--";
    private final String LINE_FEED = "\r\n";
    private final String BOUNDARY = "apiclient-" + System.currentTimeMillis();

    private Response.Listener<NetworkResponse> mListener;
    private Response.ErrorListener mErrorListener;
    private HttpOption option;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public VolleyMultipartRequest(int method, String url, HttpOption option,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.option = option;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (option.getHeaders() != null) {
            return option.getHeaders();
        }
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (option.getParams() != null) {
            return option.getParams();
        }
        return super.getParams();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; BOUNDARY=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
//        MultiPartUtil mu = new MultiPartUtil();

        try {
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, "UTF-8");
//                mu.textParse(params, "UTF-8");
            }

            Map<String, FileDataPart> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
//                mu.dataParse(data);
            }

            dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_FEED);
//            mu.flush();

//            Log.i("array", Arrays.toString(mu.getBos().toByteArray()));

            return bos.toByteArray();
//            return mu.getBos().toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Map<String, FileDataPart> getByteData() throws AuthFailureError {
        if (option.getByteData() != null) {
            return option.getByteData();
        }
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void dataParse(DataOutputStream dataOutputStream, Map<String, FileDataPart> data) throws IOException {
        for (Map.Entry<String, FileDataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    // Text Type의 데이터 생성
    // 한글 데이터를 전송할 때는 UTF-8 설정을 해야함
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        // 데이터의 경계를 표시하기 위함
        dataOutputStream.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + LINE_FEED);
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.write((parameterValue + LINE_FEED).getBytes());
        dataOutputStream.flush();
    }

    // Data Type의 데이터 생성(이미지, 영상 등)
    private void buildDataPart(DataOutputStream dataOutputStream, FileDataPart dataFile, String inputName) throws IOException {
        // 데이터의 경계를 표시하기 위함
        dataOutputStream.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_FEED);
        // 데이터의 타입 설정
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + LINE_FEED);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + LINE_FEED);
        }
        // 이후에는 데이터가 입력 됨
        dataOutputStream.writeBytes("Content-Transfer-Encoding: binary" + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.flush();
    }
}