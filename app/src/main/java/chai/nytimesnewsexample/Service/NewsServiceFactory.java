package chai.nytimesnewsexample.Service;

import chai.nytimesnewsexample.NewsConstant;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by chai on 8/6/18.
 */

public class NewsServiceFactory {
    static NewsService newsService;
    public static NewsService getInstance(){
        if(newsService == null)
            newsService = createNewsService();
        return newsService;
    }

    private static NewsService createNewsService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsConstant.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(NewsService.class);
    }
}
