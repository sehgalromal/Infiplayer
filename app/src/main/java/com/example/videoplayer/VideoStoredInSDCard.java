package com.example.videoplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VideoStoredInSDCard extends Activity implements SearchView.OnQueryTextListener {
    private Cursor videocursor;
    private int video_column_index;
    ListView videolist;
    int count;
    String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Thumbnails.VIDEO_ID};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init_phone_video_grid();
    }





    @SuppressWarnings("deprecation")
    private void init_phone_video_grid() {
        System.gc();
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE};
        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);
        count = videocursor.getCount();
        videolist = (ListView) findViewById(R.id.PhoneVideoList);
        videolist.setAdapter(new VideoAdapter(getApplicationContext()));
        videolist.setOnItemClickListener(videogridlistener);
    }

    private OnItemClickListener videogridlistener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position,
                                long id) {
            System.gc();
            video_column_index = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            videocursor.moveToPosition(position);
            String filename = videocursor.getString(video_column_index);
            Intent intent = new Intent(VideoStoredInSDCard.this,
                    ViewVideo.class);
            intent.putExtra("videofilename", filename);
            startActivity(intent);
        }
    };

    public class VideoAdapter extends BaseAdapter {
        private Context vContext;

        public VideoAdapter(Context c) {
            vContext = c;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            System.gc();
            ViewHolder holder;
            String id;
            convertView=null;
            try {

            if(convertView==null) {

                convertView = LayoutInflater.from(vContext).inflate(
                        R.layout.listitem, parent, false);
                holder = new ViewHolder();
                holder.txtTitle = (TextView) convertView
                        .findViewById(R.id.txtTitle);
                holder.txtSize = (TextView) convertView
                        .findViewById(R.id.txtSize);
                holder.thumbImage = (ImageView) convertView
                        .findViewById(R.id.imgIcon);

                video_column_index = videocursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                videocursor.moveToPosition(position);
                id = videocursor.getString(video_column_index);
                video_column_index = videocursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                videocursor.moveToPosition(position);
                int sz = (Integer.parseInt(videocursor.getString(video_column_index)) / 1024) / 1024;
                String size = sz + "MB";
                holder.txtTitle.setText(id);
                holder.txtSize.setText(size);

                String[] project = {
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DATA};
                // @SuppressWarnings("deprecation")
                Cursor cursor;

                cursor = getContentResolver().query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, project,
                        MediaStore.Video.Media.DISPLAY_NAME + "=?",
                        new String[]{id}
                        , "_display_name");
                if (cursor != null)
                    cursor.moveToFirst();
                long ids = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));

                ContentResolver crThumb = getContentResolver();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(
                        crThumb, ids, MediaStore.Video.Thumbnails.MICRO_KIND,
                        options);
                holder.thumbImage.setImageBitmap(curThumb);
                curThumb = null;

                cursor.close();
            }
            }catch (Exception e){
                Toast.makeText(VideoStoredInSDCard.this,"INTERNAL ERROR",Toast.LENGTH_LONG).show();
            }


            return convertView;
        }
    }

    static class ViewHolder {

        TextView txtTitle;
        TextView txtSize;
        ImageView thumbImage;
    }



    public boolean onQueryTextChange(String newText) {
        // this is your adapter that will be filtered
        if (TextUtils.isEmpty(newText)) {
            videolist.clearTextFilter();
        } else {
            videolist.setFilterText(newText);
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        return false;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_stored_in_sdcard, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_tings:
                Intent intent = new Intent(this, SettingActivity.class);
                this.startActivity(intent);
                break;
            case R.id.About_Us:
                Intent intent2=new Intent(this,AboutUs.class);
                this.startActivity(intent2);
                // another startActivity, this is for item with id "menu_item2"
                break;
            case R.id.downloader:
                Toast.makeText(this,"downloader Selected",Toast.LENGTH_LONG).show();
                Intent downloader = new Intent(VideoStoredInSDCard.this,Downloader.class);
                startActivity(downloader);
                return true;
            case R.id.youTube:
                Toast.makeText(this,"YOUTUBE Selected",Toast.LENGTH_LONG).show();
                Intent youtube = new Intent(VideoStoredInSDCard.this,YouTube.class);
                startActivity(youtube);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

}