package com.mdg.finalblog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =  findViewById(R.id.navigation);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth =FirebaseAuth.getInstance();

        mBlogList=(RecyclerView)findViewById(R.id.blog_list);
        linearLayoutManager =new LinearLayoutManager(this);
       mBlogList.setLayoutManager(linearLayoutManager);
       mBlogList.setHasFixedSize(true);
       fetch();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        Intent intent=new Intent(MainActivity.this,Pew.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_notifications:

                        break;

                }


                return false;
            }
        });



    }




    public  static class  BlogViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;
        public TextView post_title;
        public TextView post_desc;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

          root=itemView.findViewById(R.id.list_root);
          post_title=itemView.findViewById(R.id.post_title);
          post_desc=itemView.findViewById(R.id.post_desc);

        }

        public void setPost_title(String string)
        {
            post_title.setText(string);
        }

        public void setPost_desc(String string)
        {
            post_desc.setText(string);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add:
                Intent postintent=new Intent(MainActivity.this,PostActivity.class);
                startActivity(postintent);
                break;
            case R.id.action_logout_btn:
                logOut();

                break;

        }



        return super.onOptionsItemSelected(item);
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Blog");

        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(query, new SnapshotParser<Blog>() {
                            @NonNull
                            @Override
                            public Blog parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Blog(

                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("desc").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blog_row, parent, false);

                return new BlogViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(BlogViewHolder holder, final int position, Blog model) {
                holder.setPost_title(model.getTitle());
                holder.setPost_desc(model.getDesc());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };
        mBlogList.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void sendToLogin() {
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void logOut() {

        mAuth.signOut();
        sendToLogin();
    }


}
