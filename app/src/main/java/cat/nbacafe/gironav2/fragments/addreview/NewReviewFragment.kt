package cat.nbacafe.gironav2.fragments.addreview

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.common.SharedViewModel
import cat.nbacafe.gironav2.databinding.FragmentNewReviewBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

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

        val item = sharedViewModel.getItem()

        val storage = FirebaseStorage.getInstance().reference.child(item.nomImatge + ".jpg")
        val imatge = File.createTempFile("img", "jpg")
        storage.getFile(imatge).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(imatge.absolutePath)
            binding.topImage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            binding.topImage.setImageResource(R.drawable.basketball_court)
        }

        binding.courseNameView.setText(item.nom)
        binding.coursePriceView.setText("${item.preu} €")

        return binding.root
    }

}