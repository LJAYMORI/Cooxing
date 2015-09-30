package com.ljaymori.cooxing.reply;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.CommentListResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.vo.CommentVO;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.detail.DetailActivity;

public class ReplyActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int REPLY_REQUEST_CODE = 511;

    private RecyclerView recyclerView;
    private ReplyAdapter mAdapter;

    private EditText etContent;
    private TextView tvSend;

    private RecipeVO recipeVO;
    private int pagePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        recipeVO = (RecipeVO) getIntent().getSerializableExtra(DetailActivity.RECIPE_VO);
        pagePosition = getIntent().getIntExtra(DetailActivity.PAGE_POSITION, -1);
        if (recipeVO == null || pagePosition == -1) {
            Toast.makeText(ReplyActivity.this, getString(R.string.reply_message_no_extra), Toast.LENGTH_SHORT).show();
            finish();

        } else {
            etContent = (EditText) findViewById(R.id.edit_content_reply);
            tvSend = (TextView) findViewById(R.id.text_send_reply);
            tvSend.setOnClickListener(this);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_reply);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new ReplyAdapter(this);
            recyclerView.setAdapter(mAdapter);

            initData();
        }
    }

    private void initData() {
        NetworkManager.getInstance().getComments(this, recipeVO.get_id(), new NetworkManager.OnResultListener<CommentListResult>() {
            @Override
            public void onSuccess(CommentListResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    mAdapter.init(data.datas, pagePosition);
                    recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(ReplyActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(ReplyActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_send_reply: {
                send();
                break;
            }
        }
    }

    private void send() {
        String content = etContent.getText().toString();
        if (content.length() > 0) {
            CommentVO vo = new CommentVO();
            vo.set_id(recipeVO.get_id());
            vo.setContent(content);
            vo.setPosition(pagePosition);

            NetworkManager.getInstance().writeComment(this, vo, new NetworkManager.OnResultListener<CommentListResult>() {
                @Override
                public void onSuccess(CommentListResult data) {
                    if (data.result.equals(CooxingConstant.SUCCESS)) {
                        mAdapter.init(data.datas, pagePosition);
                        recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);

                        etContent.setText("");


                    } else if (data.result.equals(CooxingConstant.FAIL)) {
                        Toast.makeText(ReplyActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(ReplyActivity.this, code + "", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void removeReply(String comment_id) {
        RemoveCommentParams params = new RemoveCommentParams();
        params.setRecipe_id(recipeVO.get_id());
        params.setComment_id(comment_id);

        NetworkManager.getInstance().removeComment(this, params, new NetworkManager.OnResultListener<CommentListResult>() {
            @Override
            public void onSuccess(CommentListResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    mAdapter.init(data.datas, pagePosition);

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(ReplyActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(ReplyActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class RemoveCommentParams {
        private String recipe_id;
        private String comment_id;

        public String getRecipe_id() {
            return recipe_id;
        }

        public void setRecipe_id(String recipe_id) {
            this.recipe_id = recipe_id;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }
    }
}
