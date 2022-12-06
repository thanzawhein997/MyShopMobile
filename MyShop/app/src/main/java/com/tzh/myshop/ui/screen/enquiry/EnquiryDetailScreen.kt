package com.tzh.myshop.ui.screen.enquiry

import android.R
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tzh.myshop.common.ulti.FloatingActionUIStates
import com.tzh.myshop.common.navigation.Route
import com.tzh.myshop.ui.shareComponent.ChoosePhotoLayout
import com.tzh.myshop.ui.shareComponent.Dimen
import com.tzh.myshop.ui.shareComponent.MyTextFieldWithTitle
import com.tzh.myshop.ui.theme.accentAmber
import com.tzh.myshop.ui.theme.primaryCharcoal
import com.tzh.myshop.ui.viewModel.EnquiryViewModel

@Composable
fun EnquiryDetailScreen(
    navController: NavController,
    viewModel: EnquiryViewModel,
    onComposing: ((state: FloatingActionUIStates) -> Unit)? = null,
    scaffoldState: ScaffoldState
) {

    val uiState by viewModel.uiState.collectAsState()
    var imageList = viewModel.imageList
    val context = LocalContext.current
    if (!uiState.error.isNullOrEmpty()) {
        Toast.makeText(context, uiState.error.toString(), Toast.LENGTH_LONG).show()
        viewModel.doneShowErrorMessage()
    }

    if (uiState.isSaveSuccess != null) {
        if (uiState.isSaveSuccess!!) {
            navController.navigate(Route.HOME.route) {
                popUpTo(Route.HOME.route) {
                    inclusive = true
                }
            }
            Toast.makeText(context, "Successfully save", Toast.LENGTH_LONG).show()
            viewModel.doneAction()
        } else {
            Toast.makeText(context, "Fail to save \nError Message : ${uiState.error}", Toast.LENGTH_LONG).show()

        }
    }
    onComposing?.invoke(FloatingActionUIStates(onFloatingActionButton = {
        FloatingActionButton(backgroundColor = primaryCharcoal, onClick = {
            viewModel.updateProduct()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_save), tint = Color.White, contentDescription = null
            )
        }
    }))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        MyTextFieldWithTitle(title = "ProductName", value = viewModel.productName, onValueChange = { viewModel.productName = it })
        Dimen.DefaultMarginHeight()

        MyTextFieldWithTitle(
            title = "Qty",
            value = "${viewModel.qty}",
            onValueChange = viewModel.qtyTextChange,
            isTypeNumber = true,
            hasSuffix = false
        )
        Dimen.DefaultMarginHeight()

        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextFieldWithTitle(
                modifier = Modifier.weight(1f),
                title = "Original Price",
                value = "${viewModel.originalPrice}",
                onValueChange = viewModel.orgPriceTextChange,
                isTypeNumber = true,
                hasSuffix = true
            )
            Dimen.DefaultMarginWidth()
            MyTextFieldWithTitle(
                modifier = Modifier.weight(1f),
                title = "Selling Price",
                value = "${viewModel.sellingPrice}",
                onValueChange = viewModel.sellingPriceTextChange,
                isTypeNumber = true,
                hasSuffix = true
            )

        }
        Dimen.DefaultMarginHeight()
        Text(text = "Profit", style = MaterialTheme.typography.h6)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimen.paddingDefault)
                .border(1.dp, accentAmber.copy(alpha = 0.5f))
        ) {
            Text(
                text = "${viewModel.profit} MMK",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.paddingDefault),
            )
        }
        ChoosePhotoLayout(
            addImage = { uri: Uri, i: Int ->
                viewModel.addImage(uri, i)
            }, imageList = imageList
        )
    }
}


