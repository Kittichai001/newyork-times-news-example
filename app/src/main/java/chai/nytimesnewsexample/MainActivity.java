package chai.nytimesnewsexample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import chai.nytimesnewsexample.Service.Example;
import chai.nytimesnewsexample.Service.NewsService;
import chai.nytimesnewsexample.Service.NewsServiceFactory;
import chai.nytimesnewsexample.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    chai.nytimesnewsexample.ListAdapter adapter;
    List<String> arrayList= new ArrayList<>();
    List<String> urlList = new ArrayList<>();
    List<String> iconList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final NewsService newsService = NewsServiceFactory.getInstance();
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Call<Example> call = newsService.search(NewsConstant.API_KEY, "newest");
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful()) {
                    Example ob = response.body();
                    for(int i=0;i<ob.response.docs.size();i++) {
                        arrayList.add(ob.response.docs.get(i).headline.print_headline);
                        urlList.add(ob.response.docs.get(i).web_url);
                        String iconUrl=null;
                        if(ob.response.docs.get(i).multimedia.size() > 0 && ob.response.docs.get(i).multimedia.get(0).url != null)
                            iconUrl = "https://www.nytimes.com/"+ob.response.docs.get(i).multimedia.get(0).url;
                        iconList.add(iconUrl);
                    }
                    prepListView();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });






    }

    private void prepListView(){
        adapter= new ListAdapter(arrayList, iconList, getApplicationContext());
        activityMainBinding.listView.setAdapter(adapter);
        activityMainBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("web_url",urlList.get(i));

                Intent showItemIntent = new Intent(getApplicationContext(),
                        NewsDescActivity.class);
                showItemIntent.putExtras(bundle);
                startActivity(showItemIntent);
            }
        });
        activityMainBinding.search.setActivated(true);
        activityMainBinding.search.setQueryHint("Type your keyword here");
        activityMainBinding.search.onActionViewExpanded();
        activityMainBinding.search.setIconified(false);
        activityMainBinding.search.clearFocus();

        activityMainBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });


    }
}
