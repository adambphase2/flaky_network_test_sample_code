package com.phase2.networkcalltestproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.Completable

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button_second).isEnabled  = false
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        networkCall()
                .subscribeOn((activity as MainActivity).schedulers.iOScheduler)
                .subscribe {
                    //Play with latency here.
                    Thread.sleep(1000)
                    activity?.runOnUiThread {
                        view?.findViewById<Button>(R.id.button_second)?.isEnabled = true
                    }
                }
    }


    private fun networkCall() : Completable {
        return Completable.complete()
    }
}