package com.example.constitutionofindia

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.IndiaCanon.constitutionofindia.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Activity_Settings_viewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Activity_Settings_viewerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: Float = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getFloat(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        param1?.let { context?.theme?.applyStyle(it, true) }
        return inflater.inflate(R.layout.fragment_activity__settings_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<TextView>(R.id.activity_settings_viewerFragment_tvHeadline).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, param2 * 20)
        }
        view.findViewById<TextView>(R.id.activity_settings_viewerFragment_tvPartNum).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, param2 * 14)
        }
        view.findViewById<TextView>(R.id.activity_settings_viewerFragment_tvPartName).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, param2 * 14)
        }
        view.findViewById<TextView>(R.id.activity_settings_viewerFragment_tvChapterNum).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, param2 * 14)
        }
        view.findViewById<TextView>(R.id.activity_settings_viewerFragment_tvChapterName).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, param2 * 14)
        }
        view.findViewById<TextView>(R.id.activity_settings_viewerFragment_tvtext).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, param2 * 14)
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Activity_Settings_viewerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: Float) =
            Activity_Settings_viewerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putFloat(ARG_PARAM2, param2)
                }
            }
    }
}