package com.yahya.newsfeedapp.view.newsdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.yahya.newsfeedapp.R
import com.yahya.newsfeedapp.extensions.loadUrl
import com.yahya.newsfeedapp.repository.remote.NewsListModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.fragment_news_detail.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsDetail : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val args : NewsDetailArgs by navArgs()
    lateinit var newsListModel: NewsListModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Newsdetail", "${args.NewsDetail}")
        newsListModel=args.NewsDetail
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ivUrlView.loadUrl(newsListModel.urlToImage ?: "")
        tvTitle.text=newsListModel.title
        tvDescription.text=newsListModel.description?:""
        var url=newsListModel.url!!
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        tvDateAndSource.text="Published on ${newsListModel?.publishedAt?.substring(0,10)} by  ${newsListModel.source?.name}"

        tvViewInBrowser.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context?.startActivity(browserIntent)!!
        }

        ivBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsDetail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsDetail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        (activity as DaggerAppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as DaggerAppCompatActivity?)?.supportActionBar?.show()
    }
}