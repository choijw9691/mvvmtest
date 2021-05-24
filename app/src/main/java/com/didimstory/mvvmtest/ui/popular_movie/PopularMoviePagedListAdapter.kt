package com.didimstory.mvvmtest.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.didimstory.mvvmtest.R
import com.didimstory.mvvmtest.data.api.POSTER_BASE_URL
import com.didimstory.mvvmtest.data.repository.NetworkState
import com.didimstory.mvvmtest.data.vo.Movie
import com.didimstory.mvvmtest.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class PopularMoviePagedListAdapter(public val context:Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffcallback()) {

    val MOVIE_VIEW_TYPE=1
    val NETWORK_VIEW_TYPE=2
    private var networkState:NetworkState?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val layoutInflater=LayoutInflater.from(parent.context)
        val view:View
        if(viewType==MOVIE_VIEW_TYPE){
            view=layoutInflater.inflate(R.layout.movie_list_item,parent,false)
            return MovieDiffcallback.MovieItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.network_state_item,parent,false)
            return MovieDiffcallback.NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
if (getItemViewType(position)==MOVIE_VIEW_TYPE){
    (holder as MovieDiffcallback.MovieItemViewHolder).bind(getItem(position),context)
}else{
    (holder as MovieDiffcallback.NetworkStateItemViewHolder).bind(networkState)
}
    }

    private fun hasExtraRow():Boolean{

        return networkState != null && networkState != NetworkState.LOADED

    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow()&&position==itemCount-1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffcallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(movie: Movie?, context: Context) {
                itemView.cv_iv_movie_title.text = movie?.title
                itemView.cv_movie_release_date.text = movie?.releaseDate

                val moviePosterURL= POSTER_BASE_URL + movie?.posterPath
                Glide.with(itemView.context)
                    .load(moviePosterURL)
                    .into(itemView.cv_iv_movie_poster)

                itemView.setOnClickListener{
                    val intent= Intent(context,SingleMovie::class.java)
                    intent.putExtra("id",movie?.id)
                    context.startActivity(intent)

                }
            }
        }
        class NetworkStateItemViewHolder ( view:View) : RecyclerView.ViewHolder(view){
            fun bind(networkState: NetworkState?){
                if (networkState != null && networkState==NetworkState.LOADING){
                    itemView.progress_bar_item.visibility=View.VISIBLE
                }
                else{
                    itemView.progress_bar_item.visibility=View.GONE
                }
                if (networkState != null && networkState==NetworkState.ERROR){
                    itemView.error_msg_item.visibility=View.VISIBLE
                    itemView.error_msg_item.text=networkState.msg
                }
                else if(networkState!=null&&networkState==NetworkState.ENDOFLIST){
                    itemView.error_msg_item.visibility=View.VISIBLE
                    itemView.error_msg_item.text=networkState.msg
                }
                else{
                    itemView.error_msg_item.visibility=View.GONE
                }
            }
        }

    }
    fun setNetworkState(newNetworkState:NetworkState){
        val previousState=this.networkState
val hadExtraRow=hasExtraRow()
        this.networkState=newNetworkState
        val hasExtraRow=hasExtraRow()
        if (hadExtraRow!=hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }
else if(hasExtraRow&&previousState!=newNetworkState){
            notifyItemChanged(itemCount,-1)
        }
    }
}