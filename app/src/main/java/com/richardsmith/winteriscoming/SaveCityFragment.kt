package com.richardsmith.winteriscoming


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_save_city.*


/**
 * A simple [Fragment] subclass.
 */
class SaveCityFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_save_city, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(activity).get(PlaceViewModel::class.java)

        saveplace.setOnClickListener {
            val placeToSave = savePlaceText.text.toString()
            val p = Place(0,placeToSave)
            viewModel.savePlace(p)
            savePlaceText.text.clear()
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, HomeFragment())
                    .commit()
        }
    }
}// Required empty public constructor
