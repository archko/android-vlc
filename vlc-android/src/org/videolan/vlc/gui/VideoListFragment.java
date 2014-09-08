package org.videolan.vlc.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.andrew.apollo.utils.ApolloUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.videolan.vlc.R;
import org.videolan.vlc.gui.video.VideoPlayerActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 图书列表,一个系列教材中的图书,如新概念英语中的美单版,四六级考试
 *
 * @author archko
 */
public class VideoListFragment extends Fragment {

    public static final String TAG="VideoListFragment";
    private Handler mHandler=new Handler();
    protected ArrayList mDataList;
    private ListView mListView;
    private VideoAdapter mAdapter;
    private LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater=LayoutInflater.from(getActivity());
        mDataList=new ArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.history_list, container, false);
        mListView=(ListView) root.findViewById(android.R.id.list);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null==mAdapter) {
            mAdapter=new VideoAdapter();
        }

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick(parent, view, position, id);
            }
        });

        onSelected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onSelected() {
        if (mDataList==null||mDataList.size()<1) {
            getVideoList();
        } else {
            if (isResumed()) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void getVideoList() {
        ApolloUtils.execute(false, new AsyncTask<Object, Object, ArrayList<Video>>() {
            @Override
            protected ArrayList<Video> doInBackground(Object... params) {
                try {
                    InputStream is=VideoListFragment.this.getActivity().getAssets().open("channelResult.json");
                    String rs=parseInputStream(is);

                    Log.d(TAG, "rs:"+rs);

                    org.json.JSONObject jsonObject=new JSONObject(rs);
                    JSONArray ja=jsonObject.optJSONArray("channelList");
                    JSONObject jo;
                    ArrayList<Video> list=new ArrayList<Video>();
                    Video bean;
                    for (int i=0; i<ja.length(); i++) {
                        jo=ja.optJSONObject(i);
                        bean=new Video();
                        bean.channel_name=jo.optString("channel_name");
                        bean.channel_url=jo.optString("channel_url");
                        list.add(bean);
                    }
                    return list;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Video> o) {
                Log.d(TAG, "onPostExecute:"+o);
                if (null==o) {
                } else {
                    mDataList.clear();
                    mDataList.addAll(o);
                    if (isResumed()) {
                        updateView(true);
                    }
                }
            }
        });
    }

    public static String parseInputStream(InputStream is) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(is), 1000);
        StringBuilder responseBody=new StringBuilder();
        String line=reader.readLine();
        while (line!=null) {
            responseBody.append(line);
            line=reader.readLine();
        }
        String string=responseBody.toString();
        return string;
    }

    protected void updateView(boolean refresh) {
        Log.v(TAG, "updateView:"+refresh+" data:"+mDataList);
        if (mDataList==null||mDataList.size()<1) {
        } else {
            if (refresh) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void itemClick(AdapterView<?> parent, View view, int position, long id) {
        Video bean=(Video) mAdapter.getItem(position);
        Log.d(TAG, "itemClick:"+position+" video:"+bean);
        startVideo(bean);
    }

    private void startVideo(Video bean) {
        VideoPlayerActivity.start(getActivity(), bean.channel_url, true);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DirectoryAdapter.DirectoryViewHolder holder;
        View v=convertView;

        /* If view not created */
        if (v==null) {
            v=mInflater.inflate(R.layout.list_item, parent, false);
            holder=new DirectoryAdapter.DirectoryViewHolder();
            holder.layout=v.findViewById(R.id.layout_item);
            holder.title=(TextView) v.findViewById(R.id.title);
            holder.text=(TextView) v.findViewById(R.id.artist);
            holder.icon=(ImageView) v.findViewById(R.id.cover);
            holder.icon.setVisibility(View.GONE);
            v.setTag(holder);
        } else
            holder=(DirectoryAdapter.DirectoryViewHolder) v.getTag();

        Video m=(Video) mAdapter.getItem(position);
        Log.d(TAG, "Loading media position "+position+" - "+m);
        holder.title.setText(m.channel_name);

        holder.text.setText(m.channel_url);

        return v;
    }

    //--------------------- adapter ---------------------
    public class VideoAdapter extends BaseAdapter {

        public VideoAdapter() {
            if (null==mDataList) {
                mDataList=new ArrayList();
            }
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return VideoListFragment.this.getView(position, convertView, parent);
        }
    }
}
