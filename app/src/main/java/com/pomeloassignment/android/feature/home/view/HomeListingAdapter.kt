package com.pomeloassignment.android.feature.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.pomeloassignment.android.R
import com.pomeloassignment.android.base.BaseRecyelerviewAdapter
import com.pomeloassignment.android.base.BaseViewHolder
import com.pomeloassignment.android.db.ArticleEntity

class HomeListingAdapter(private val clickListener: (item: ArticleEntity, pos: Int) -> Unit) :
    BaseRecyelerviewAdapter<ArticleEntity>() {

    override val callback: (input: List<ArticleEntity>) -> DiffUtil.Callback
        get() = { input: List<ArticleEntity> ->
            object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = getData().size

                override fun getNewListSize(): Int = input.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return getData()[oldItemPosition].id == input[newItemPosition].id
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return getData()[oldItemPosition] == input[newItemPosition]
                }
            }
        }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_news_item, parent, false)
        return ArticleItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            clickListener.invoke(getData()[position], position)
        }
    }


    class ArticleItemViewHolder(view: View) : BaseViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.news_article_banner_image)
        val title: TextView = view.findViewById(R.id.title)
        val abstract: TextView = view.findViewById(R.id.news_abstract)


        override fun onBind(data: Any?) {
            val article = data as? ArticleEntity ?: return
            title.text = article.title
            abstract.text = article.abstract

            Glide.with(view.context)
                .load(article.imageUrl)
                .into(image)
        }

        override fun destroy() {
            image.destroyDrawingCache()
        }
    }
}