package com.lcc.imusic.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.PlayListAdapter;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.MusicList;
import com.lcc.imusic.utils.PrfUtil;
import com.lcc.imusic.wiget.StateLayout;

import java.util.List;

import butterknife.Bind;

public class MusicListActivity extends BaseActivity {


    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<MusicList> musicLists;

    PlayListAdapter playListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateLayout.setEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMusicList();
            }
        });

        musicLists = MusicList.listAll(MusicList.class);
        if (musicLists.isEmpty()) {
            stateLayout.showEmptyView("还没有歌单吆");
        } else {
            initRecy();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_list;
    }


    private void addMusicList() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        final View view = getLayoutInflater().inflate(R.layout.add_music_list, null);
        dialog.setContentView(view);
        view.findViewById(R.id.bt_music_list_add)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) view.findViewById(R.id.et_music_list_id);
                        long id = Long.parseLong(editText.getText().toString());
                        if (!musicLists.isEmpty()) {
                            List<MusicList> musicLists = MusicList.find(MusicList.class, "musicListId = ?", id + "");
                            if (!musicLists.isEmpty()) {
                                toast("已经添加此歌单");
                                dialog.dismiss();
                                return;
                            }
                        }
                        MusicList musicList = new MusicList();
                        musicList.musicListId = id;
                        musicList.save();


                        /**重新读取数据*/
                        musicLists = MusicList.listAll(MusicList.class);
                        if (playListAdapter == null) {
                            initRecy();
                        } else {
                            playListAdapter.setData(musicLists);
                        }


                        dialog.dismiss();
                    }
                });

        dialog.setTitle("添加歌单ID");
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            addMusicList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecy() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        playListAdapter = new PlayListAdapter();
        recyclerView.setAdapter(playListAdapter);
        playListAdapter.setData(musicLists);
        playListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                long id = playListAdapter.getData(position).musicListId;
                PrfUtil.start().putLong("current_play_list", id).commit();
                toast("设置成功");
            }
        });
        stateLayout.showContentView();
    }
}
