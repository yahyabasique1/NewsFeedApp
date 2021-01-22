package com.yahya.newsfeedapp.view.newslist

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahya.newsfeedapp.R
import com.yahya.newsfeedapp.di.injectViewModel
import com.yahya.newsfeedapp.view.newslist.adapter.LoaderStateAdapter
import com.yahya.newsfeedapp.view.newslist.adapter.NewsListAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_newslist.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
class NewsListFragment:DaggerFragment() {

    lateinit var newsViewModel:NewsViewModel
     var newsListAdapter: NewsListAdapter= NewsListAdapter()
    lateinit var loaderStateAdapter: LoaderStateAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                Log.e("called1","onViewCreated")


    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("called1","onActivityCreated")
        setHasOptionsMenu(true)
        initMembers()
        setUpViews()
        val lastsearchedquery=sharedPreferences.getString("lastSearchedQuery","")?:""
        Log.e("LASTSEARCH",lastsearchedquery)
        if(lastsearchedquery.isEmpty()){
            fetchNews()
        }else{
            fetchNews(lastsearchedquery)
        }

//        lifecycleScope.launch {
//            newsListAdapter.loadStateFlow
//                    // Only emit when REFRESH LoadState for RemoteMediator changes.
//                    .distinctUntilChangedBy { it.refresh }
//                    // Only react to cases where Remote REFRESH completes i.e., NotLoading.
//                    .filter {
//                        it.refresh is LoadState.NotLoading
//                    }.collect {
//                        rvDoggoLoader.scrollToPosition(0)
//                    }
//        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("called1","onCreate")
//        sharedPreferences=context?.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)!!

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("called1","onCreateView")
        val view=inflater.inflate(R.layout.fragment_newslist,container,false)

        return view
    }

     fun fetchNews(query:String="Cristiano Ronaldo") {
       CoroutineScope(Dispatchers.Main).launch {
           newsViewModel.fetchDoggoImages(query).observe(viewLifecycleOwner, Observer {
//               newsListAdapter.refresh()
               newsListAdapter.submitData(lifecycle,it)
           })
//           newsViewModel.fetchDoggoImages(query).collectLatest {
//               newsListAdapter.submitData(it)
//
//
//                         }
       }
    }

    private fun setUpViews() {
        rvNewsFeedLoader.layoutManager=LinearLayoutManager(context)
        rvNewsFeedLoader.adapter=newsListAdapter.withLoadStateFooter(loaderStateAdapter)
    }

    private fun initMembers() {
//        newsViewModel = defaultViewModelProviderFactory.create(NewsViewModel::class.java)
        newsViewModel = injectViewModel(viewModelFactory)
        loaderStateAdapter= LoaderStateAdapter {
            newsListAdapter.retry()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
        val  searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        configureSearch(searchView)
    }

    private fun configureSearch(searchView: SearchView) {
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchNews(query?:"")
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.setIconified(true);
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                editor.putString("lastSearchedQuery",query)
                editor.apply()
                editor.commit()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.e("called1","onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e("called1","onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("called1","onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("called1","onDestroyView")

    }
}