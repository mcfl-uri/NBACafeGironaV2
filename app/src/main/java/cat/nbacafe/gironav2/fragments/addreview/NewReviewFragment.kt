package cat.nbacafe.gironav2.fragments.addreview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.common.SharedViewModel
import cat.nbacafe.gironav2.databinding.FragmentNewReviewBinding

class NewReviewFragment : Fragment() {

    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentNewReviewBinding>(
            inflater,
            R.layout.fragment_new_review, container, false
        )

        Toast.makeText(context, sharedViewModel.getItem().nom, Toast.LENGTH_LONG).show()

        return binding.root
    }

}