package com.example.lijia.testrecyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ScaleRecyclerView mRecyclerView;
    private TestMyAdapter myAdapter;
    List<DataModel> stringList = new ArrayList<DataModel>();

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (ScaleRecyclerView) findViewById(R.id.my_recycler_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        initData();
        //创建横向线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.supportsPredictiveItemAnimations();
        mRecyclerView.setLayoutManager(layoutManager);

        myAdapter = new TestMyAdapter(this,stringList);
        mRecyclerView.setAdapter(myAdapter);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 10;
                outRect.left = 10;
            }
        });

        View headView = LayoutInflater.from(this).inflate(R.layout.adapter_header, mRecyclerView, false);
        myAdapter.setHeaderView(headView);

        TextView footerView = new TextView(this);
        footerView.setText("footer");
        footerView.setTextColor(Color.RED);
        myAdapter.setFooterView(footerView);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringList.add(new DataModel("Test",TestMyAdapter.ITEM_NORMAL));
                myAdapter.notifyDataSetChanged();
//                Intent intent = new Intent(MainActivity.this
//                        , TestActivity.class);
//                startActivity(intent);

            }
        });
    }

    //初始化数据
    public void initData() {
        for (int i = 0; i < 10; i++) {
            DataModel dm = new DataModel();
            dm.name = "data:" + i;
            dm.type = TestMyAdapter.ITEM_NORMAL;
            if (i == 5) {
                dm.type = TestMyAdapter.ITEM_HEAD;
            }
            stringList.add(dm);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    class TestMyAdapter extends RecyclerView.Adapter<TestMyAdapter.MyViewHolder> {

        private static final int ITEM_NORMAL = 0;
        private static final int ITEM_HEAD = 1;
        private static final int ITEM_FOOTER = -1;

        private View mHeaderView;

        private View mFooterView;
        private List<DataModel> mList = new ArrayList<DataModel>();

        private HashMap<Integer,Boolean> selectedMap = new HashMap<>();

        private int selectedPos = -1;

        Animation zoomin;

        public TestMyAdapter(Context con,List<DataModel> mList) {
            this.mList = mList;
//            zoomin = AnimationUtils.loadAnimation(con, R.anim.zoomin);
            notifyItemInserted(0);
        }

        public void setHeaderView(View headerView) {
            this.mHeaderView = headerView;
            notifyDataSetChanged();
        }

        public void setFooterView (View footerView) {
            this.mFooterView = footerView;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyViewHolder viewHolder = null;
            if (viewType == ITEM_HEAD) {
                viewHolder = new MyViewHolder(mHeaderView);
            } else if (viewType == ITEM_FOOTER) {
                viewHolder = new MyViewHolder(mFooterView);
            } else {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.adapter_text, null);
                viewHolder = new MyViewHolder(view);
            }
//            viewHolder.setIsRecyclable(false);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if(getItemViewType(position) == ITEM_HEAD) {
                return;
            }
            if (getItemViewType(position) == ITEM_FOOTER) {
                return;
            }
            final int realPosition = getRealPostion(holder);
            String name = mList.get(realPosition).name;
            if (holder instanceof MyViewHolder) {
                holder.textView.setText(name);
            }

            holder.closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("TAG", "onClick: del del del " + realPosition);
                    Log.i("TAG", "onClick before: " + mList.size());
                    notifyItemRemoved(realPosition);
                    mList.remove(realPosition);
                    Log.i("TAG", "onClick after: " + mList.size());
                    notifyDataSetChanged();
                    if (realPosition != mList.size()) {
//                        notifyItemRangeChanged(0,mList.size() - realPosition);
                    }

                    holder.closeImageView.setVisibility(View.GONE);
                    ViewCompat.animate(v).scaleX(1f).scaleY(1f).translationZ(1).start();
                }
            });

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.i("TAG", "onFocusChange: " + hasFocus);
                    if (!hasFocus) {
                        holder.closeImageView.setVisibility(View.GONE);
                        ViewCompat.animate(v).scaleX(1f).scaleY(1f).translationZ(1).start();
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("TAG", "onLongClick: " + realPosition);
                    holder.closeImageView.setVisibility(View.VISIBLE);
                    selectedPos = realPosition;

                    ViewCompat.animate(v).scaleX(2f).scaleY(1.2f).translationZ(1).start();
                    return false;
                }
            });

        }

        int getRealPostion(RecyclerView.ViewHolder viewHolder){
            int positon = viewHolder.getLayoutPosition();
            return mHeaderView == null ? positon : positon -1;
        }


        @Override
        public int getItemViewType(int position) {
            if (mHeaderView != null && position == 0) {
                return ITEM_HEAD;
            }
            if (mFooterView != null && position == (getItemCount() - 1)) {
                return ITEM_FOOTER;
            }
            return ITEM_NORMAL;
        }

        @Override
        public int getItemCount() {
            return mHeaderView == null ? mList.size() : mList.size() + 1;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            ImageView closeImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                if (itemView == mHeaderView || itemView == mFooterView) return;

                textView = (TextView) itemView.findViewById(R.id.my_tv);
                closeImageView = (ImageView) itemView.findViewById(R.id.my_del_imv);
            }

        }
    }

}
