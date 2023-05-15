package com.aboutcapsule.android.views.capsule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.ArticleRecyclerItemBinding
import com.aboutcapsule.android.databinding.CommentsRecyclerItemBinding
import com.bumptech.glide.Glide

class CapsuleArticleInBoardAdapter : RecyclerView.Adapter<CapsuleArticleInBoardAdapter.ViewHolder>() {
        var itemList = mutableListOf<String>()// 자료형 data에서 가져오기

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ArticleRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.bind(itemList[position])
        }

        inner class ViewHolder(val binding: ArticleRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){

            private val innerRecyclerView = binding.articleRecylcerItemCommentsRecylcerView

            fun bind(commentsData: CommentsData) {
//                Glide.with(itemView).load(commentsData.유저프로필).into(binding.articleRecylcerItemUserimg)
//                Glide.with(itemView).load(commentsData.컨텐츠사진).into(binding.articleRecylcerItemContentimg)
//                binding.articleRecylcerItemTitle.text=commentsData.타이틀
//                binding.articleRecylcerItemDate.text=commentsData.날짜
//                binding.articleRecylcerItemNickname.text=commentsData.닉네임
//                binding.articleRecylcerItemContent.text=commentsData.컨텐츠
//                binding.articleRecylcerItemCommentsRecylcerView.
                val layoutManager = LinearLayoutManager(itemView.context)
                innerRecyclerView.layoutManager=layoutManager
//                val adapter = InnerAdapter(inneritems)

            }
        }

        // 댓글 리사이클러뷰에 매핑
        class InnerViewHolder(val binding: CommentsRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(commentsData: CommentsData) {
//                Glide.with(itemView).load(commentsData.댓글유저프로필).into(binding.commentsItemImg)
//                  binding.commentsItemName.text = commentsData.댓글유저이름
//                binding.commentsItemDate.text = commentsData.댓글유저날짜
//                    binding.commentsItemComment.text = commentsData.유저댓글
            }
        }

    class InnerAdapter() : RecyclerView.Adapter<InnerViewHolder>() {
        private val innerItemList = mutableListOf<String>() // 댓글 리스트

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val binding = CommentsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return InnerViewHolder(binding)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
//            holder.bind(innerItemList[position])
        }

        // inner recyclerItem count / 댓글 수
        override fun getItemCount() :Int {
            return innerItemList.size
        }

    }


    // outer recyclerItem count / 게시글 수
        override fun getItemCount(): Int {
            return itemList.count()
        }

}