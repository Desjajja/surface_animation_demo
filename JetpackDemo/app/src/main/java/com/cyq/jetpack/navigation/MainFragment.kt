package com.cyq.jetpack.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.cyq.jetpack.R
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.btnSkip.setOnClickListener {
            //方式1
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putString("username", "张三")
            bundle.putInt("age", 23)
            navController.navigate(R.id.action_mainFragment_to_secondFragment, bundle)
        }

        //方式2：使用safe args插件
        rootView.btnSkip2.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = MainFragmentArgs.Builder()
                .setUsername("李四")
                .setAge(19)
                .build().toBundle()
            navController.navigate(R.id.action_mainFragment_to_secondFragment, bundle)
        }
        return rootView
    }
}