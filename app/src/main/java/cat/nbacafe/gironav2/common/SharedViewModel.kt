package cat.nbacafe.gironav2.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private var _selectedItem = MutableLiveData("")

    val selectedItem: LiveData<String> = _selectedItem

    fun setItem(item: String) {
        _selectedItem.value = item
    }

    fun getItem() : String {
        return _selectedItem.value.toString()
    }
}