package com.aboutcapsule.android.views.capsule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.memory.CommentRes
import com.aboutcapsule.android.data.memory.MemoryDetailDto
import com.aboutcapsule.android.data.memory.MemoryRes
import com.aboutcapsule.android.databinding.ArticleRecyclerItemBinding
import com.aboutcapsule.android.databinding.CommentsRecyclerItemBinding
import com.bumptech.glide.Glide
import org.w3c.dom.Comment

class CapsuleArticleInBoardAdapter : RecyclerView.Adapter<CapsuleArticleInBoardAdapter.ViewHolder>() {
        var itemList = mutableListOf<MemoryDetailDto>()// 자료형 data에서 가져오기

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ArticleRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(itemList[position])
        }

        inner class ViewHolder(val binding: ArticleRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){

            private val innerRecyclerView = binding.articleRecylcerItemCommentsRecylcerView

            fun bind(memoryDetailDto: MemoryDetailDto) {
                Glide.with(itemView).load(memoryDetailDto.profileImageUrl).into(binding.articleRecylcerItemUserimg)
                Glide.with(itemView).load(memoryDetailDto.imageUrl).into(binding.articleRecylcerItemContentimg)
                binding.articleRecylcerItemTitle.text=memoryDetailDto.memoryTitle
                val dataString = memoryDetailDto.createdDate.toString()
                val arr = dataString.split("-")
                val year = arr[0]
                val month = arr[1]
                val day = arr[2]
                binding.articleRecylcerItemDate.text="${year}년 ${month}월 ${day}일"
                binding.articleRecylcerItemNickname.text=memoryDetailDto.nickname
                binding.articleRecylcerItemContent.text=memoryDetailDto.content
//                binding.articleRecylcerItemCommentsRecylcerView.
                val layoutManager = LinearLayoutManager(itemView.context)
                innerRecyclerView.layoutManager=layoutManager
//                val adapter = InnerAdapter(inneritems)

            }
        }

        // 댓글 리사이클러뷰에 매핑
        class InnerViewHolder(val binding: CommentsRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(commentRes: CommentRes) {
                Glide.with(itemView).load(commentRes.profileImageUrl).into(binding.commentsItemImg)
                binding.commentsItemName.text = commentRes.nickname
                val dateArrays = commentRes.createdDate.split("-")
                val year = dateArrays[0]
                val month = dateArrays[1]
                val days = dateArrays[2]
                binding.commentsItemDate.text = "${year}년 ${month}월 ${days}일"
                binding.commentsItemComment.text = commentRes.content
            }
        }

    class InnerAdapter() : RecyclerView.Adapter<InnerViewHolder>() {
        private val innerItemList = mutableListOf<CommentRes>() // 댓글 리스트

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val binding = CommentsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return InnerViewHolder(binding)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            holder.bind(innerItemList[position])
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