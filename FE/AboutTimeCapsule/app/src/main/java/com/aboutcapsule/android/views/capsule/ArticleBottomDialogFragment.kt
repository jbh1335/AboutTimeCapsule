package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentArticleBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ArticleBottomDialogFragment(val itemClick:(Int)-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentArticleBottomDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article_bottom_dialog,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.articleBottomDialogModify.setOnClickListener{
            itemClick(0)
            dialog?.dismiss()
        }

        binding.articleBottomDialogDelete.setOnClickListener{
            itemClick(1)
            dialog?.dismiss()
        }

    }

}