package com.ljaymori.cooxing.common.manager;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.auth.result.CommentListResult;
import com.ljaymori.cooxing.auth.result.IngredientResult;
import com.ljaymori.cooxing.auth.result.NetworkResult;
import com.ljaymori.cooxing.auth.result.RecipeListResult;
import com.ljaymori.cooxing.auth.result.RecipeResult;
import com.ljaymori.cooxing.auth.result.StringResult;
import com.ljaymori.cooxing.auth.result.UserListResult;
import com.ljaymori.cooxing.auth.result.UserPopResult;
import com.ljaymori.cooxing.auth.result.UserResult;
import com.ljaymori.cooxing.common.CooxingApplication;
import com.ljaymori.cooxing.common.utils.ResizeUtils;
import com.ljaymori.cooxing.common.vo.CommentVO;
import com.ljaymori.cooxing.common.vo.IngredientVO;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.common.vo.UserVO;
import com.ljaymori.cooxing.reply.ReplyActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class NetworkManager implements NetworkConstant {
    private static final String TAG = NetworkManager.class.getSimpleName();

    // singleton
    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    AsyncHttpClient client;

    private NetworkManager() {

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(socketFactory);
            client.setCookieStore(new PersistentCookieStore(CooxingApplication.getContext()));
            client.setTimeout(20000);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    public HttpClient getHttpClient() {
        return client.getHttpClient();
    }

    public interface OnResultListener<T> {
        public void onSuccess(T data);

        public void onFail(int code);
    }

    /**
     * 구현 부분
     */

    // 일반 회원가입
    public void signup(Context context, UserVO user, final OnResultListener<UserResult> listener) {
        String url = HTTPS_URL + PROTOCOL_SIGNUP;

        RequestParams params = new RequestParams();
        params.put(PARAM_USERID, user.getUserID());
        params.put(PARAM_PW, user.getPw());
        params.put(PARAM_NICKNAME, user.getNickname());
        params.put(PARAM_PUSHKEY, user.getPushKey());

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                UserResult res = gson.fromJson(responseString, UserResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 일반 로그인
    public void login(Context context, UserVO user, final OnResultListener<UserResult> listener) {
        String url = HTTPS_URL + PROTOCOL_LOGIN;

        RequestParams params = new RequestParams();
        params.put(PARAM_USERID, user.getUserID());
        params.put(PARAM_PW, user.getPw());
        params.put(PARAM_PUSHKEY, user.getPushKey());

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                UserResult res = gson.fromJson(responseString, UserResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 관심 재료 리스트 받아오기
    public void getIngredients(Context context, final OnResultListener<IngredientResult> listener) {
        String url = HTTPS_URL + PROTOCOL_GET_INGREDIENTS;

//        RequestParams params = new RequestParams();

        client.get(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                IngredientResult res = gson.fromJson(responseString, IngredientResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 관심 재료 선택하기
    public void selectIngredients(Context context, ArrayList<String> ingredients, final OnResultListener<UserResult> listener) {
        String url = HTTPS_URL + PROTOCOL_SELECT_INGREDIENTS;

        RequestParams params = new RequestParams();
        for (String ingredient : ingredients) {
            params.add(PARAM_INGREDIENTS, ingredient);
        }

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                UserResult res = gson.fromJson(responseString, UserResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 레시피 받아오기
    public void recipeList(Context context, final OnResultListener<RecipeListResult> listener) {
        String url = HTTPS_URL + PROTOCOL_RECIPE_LIST;

//        RequestParams params = new RequestParams();

        client.post(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                RecipeListResult res = gson.fromJson(responseString, RecipeListResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 레시피 하나 받아오기
    public void getRecipe(Context context, String recipe_id, final OnResultListener<RecipeResult> listener) {
        String url = HTTPS_URL + PROTOCOL_GET_RECIPE;

        RequestParams params = new RequestParams();
        params.put(PARAM_RECIPE_ID, recipe_id);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                RecipeResult res = gson.fromJson(responseString, RecipeResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 내가 좋아요한 레시피 목록 받아오기
    public void cookList(Context context, final OnResultListener<RecipeListResult> listener) {
        String url = HTTP_URL + PROTOCOL_COOK_LIST;

//        RequestParams params = new RequestParams();

        client.post(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                RecipeListResult res = gson.fromJson(responseString, RecipeListResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 유저 프로필 이미지 바꾸기
    public void registerProfileImage(Context context, String filename, final OnResultListener<StringResult> listener) {
        try {
            String url = HTTP_URL + PROTOCOL_REGISTER_PROFILE_IMAGE;

            RequestParams params = new RequestParams();
            params.put(PARAM_PROFILE_IMAGE, new File(filename));

            client.post(context, url, params, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Gson gson = new Gson();
                    StringResult res = gson.fromJson(responseString, StringResult.class);

                    listener.onSuccess(res);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    listener.onFail(statusCode);
                }
            });


        } catch (FileNotFoundException e) {
            Toast.makeText(context, context.getString(R.string.profile_message_not_found_file_path), Toast.LENGTH_SHORT).show();
        }
    }

    // 레시피 등록하기
    public void registerRecipe(Context context, RecipeVO recipe, final OnResultListener<NetworkResult> listener) {
        try {
            String url = HTTP_URL + PROTOCOL_REGISTER_RECIPE;

            RequestParams params = new RequestParams();
            params.put(PARAM_TITLE, recipe.getTitle());
            params.put(PARAM_DESCRIPTION, recipe.getDescription());
            for (IngredientVO ingredient : recipe.getIngredients()) {
                params.add(PARAM_INGREDIENTS, ingredient.getName());
            }
            for (String hashtag : recipe.getHashtags()) {
                params.add(PARAM_HASHTAGS, hashtag);
            }

            final ArrayList<String> resizedPaths = new ArrayList<String>();
            for (String image : recipe.getImages()) {
                String resizedPath = ResizeUtils.getResizedPath(image);

                params.put(PARAM_IMAGES, new File(resizedPath));
                resizedPaths.add(resizedPath);
            }


            JSONArray jsonArray = new JSONArray();
            for (String step : recipe.getSteps()) {
                jsonArray.put(step);
            }
            params.add(PARAM_STEPS, jsonArray.toString());

            client.post(context, url, params, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    removeFiles(resizedPaths);
                    Gson gson = new Gson();
                    NetworkResult res = gson.fromJson(responseString, NetworkResult.class);

                    listener.onSuccess(res);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    listener.onFail(statusCode);
                }
            });

        } catch (FileNotFoundException e) {
            Toast.makeText(context, context.getString(R.string.write_message_network_transfer), Toast.LENGTH_SHORT).show();
        }

    }
    private void removeFiles(ArrayList<String> filePaths) {
        for(String path : filePaths) {
            File f = new File(path);
            if(f.exists()) {
                f.delete();
            }
        }
    }


    // 레시피 좋아요
    public void likeRecipe(Context context, String recipe_id, final OnResultListener<RecipeResult> listener) {
        String url = HTTP_URL + PROTOCOL_LIKE_RECIPE;

        RequestParams params = new RequestParams();
        params.put(PARAM_RECIPE_ID, recipe_id);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                RecipeResult res = gson.fromJson(responseString, RecipeResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 레시피 좋아요 취소
    public void unlikeRecipe(Context context, String recipe_id, final OnResultListener<RecipeResult> listener) {
        String url = HTTP_URL + PROTOCOL_UNLIKE_RECIPE;

        RequestParams params = new RequestParams();
        params.put(PARAM_RECIPE_ID, recipe_id);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                RecipeResult res = gson.fromJson(responseString, RecipeResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 댓글 가져오기
    public void getComments(Context context, String recipe_id, final OnResultListener<CommentListResult> listener) {
        String url = HTTP_URL + PROTOCOL_GET_COMMENTS;

        RequestParams params = new RequestParams();
        params.put(PARAM_RECIPE_ID, recipe_id);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                CommentListResult res = gson.fromJson(responseString, CommentListResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 댓글 쓰기
    public void writeComment(Context context, CommentVO commentVO, final OnResultListener<CommentListResult> listener) {
        String url = HTTP_URL + PROTOCOL_WRITE_COMMENT;

        RequestParams params = new RequestParams();
        params.put(PARAM_RECIPE_ID, commentVO.get_id());
        params.put(PARAM_CONTENT, commentVO.getContent());
        params.put(PARAM_POSITION, commentVO.getPosition() + "");

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                CommentListResult res = gson.fromJson(responseString, CommentListResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 댓글 삭제하기
    public void removeComment(Context context, ReplyActivity.RemoveCommentParams ids, final OnResultListener<CommentListResult> listener) {
        String url = HTTP_URL + PROTOCOL_REMOVE_COMMENT;

        RequestParams params = new RequestParams();
        params.put(PARAM_RECIPE_ID, ids.getRecipe_id());
        params.put(PARAM_COMMENT_ID, ids.getComment_id());

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                CommentListResult res = gson.fromJson(responseString, CommentListResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 유저 한 명 정보 가져오기
    public void getUser(Context context, String user_id, final OnResultListener<UserPopResult> listener) {
        String url = HTTP_URL + PROTOCOL_GET_USER;

        RequestParams params = new RequestParams();
        params.put(PARAM_USER_ID, user_id);

        client.post(context, url, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                UserPopResult res = gson.fromJson(responseString, UserPopResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 팔로우
    public void follow(Context context, String follower_id, final OnResultListener<NetworkResult> listener) {
        String url = HTTP_URL + PROTOCOL_FOLLOW;

        RequestParams params = new RequestParams();
        params.put(PARAM_FOLLOWER_ID, follower_id);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                NetworkResult res = gson.fromJson(responseString, NetworkResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 언팔로우
    public void unfollow(Context context, String follower_id, final OnResultListener<NetworkResult> listener) {
        String url = HTTP_URL + PROTOCOL_UNFOLLOW;

        RequestParams params = new RequestParams();
        params.put(PARAM_FOLLOWER_ID, follower_id);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                NetworkResult res = gson.fromJson(responseString, NetworkResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 레시피 검색하기
    public void searchRecipe(Context context, String keyword, final OnResultListener<RecipeListResult> listener) {
        String url = HTTP_URL + PROTOCOL_SEARCH_RECIPE;

        RequestParams params = new RequestParams();
        params.put(PARAM_KEYWORD, keyword);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                RecipeListResult res = gson.fromJson(responseString, RecipeListResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

    // 유저 검색하기
    public void searchUser(Context context, String keyword, final OnResultListener<UserListResult> listener) {
        String url = HTTP_URL + PROTOCOL_SEARCH_USER;

        RequestParams params = new RequestParams();
        params.put(PARAM_KEYWORD, keyword);

        client.post(context, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                UserListResult res = gson.fromJson(responseString, UserListResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }


    // 로그아웃
    public void logout(Context context, final OnResultListener<NetworkResult> listener) {
        String url = HTTP_URL + PROTOCOL_LOGOUT;

//        RequestParams params = new RequestParams();

        client.post(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                NetworkResult res = gson.fromJson(responseString, NetworkResult.class);

                listener.onSuccess(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
        });
    }

}
