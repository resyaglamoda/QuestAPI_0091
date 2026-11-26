package com.example.mydatasiswa.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydatasiswa.R
import com.example.mydatasiswa.modeldata.DataSiswa
import com.example.mydatasiswa.uicontroller.route.DestinasiHome
import com.example.mydatasiswa.viewmodel.HomeViewModel
import com.example.mydatasiswa.viewmodel.PenyediaViewModel
import com.example.mydatasiswa.viewmodel.StatusUiSiswa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = PenyediaViewModel.Factory
    )
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiHome.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.padding_large)
                )
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    ) { innerPadding ->

        HomeBody(
            statusUiSiswa = viewModel.listSiswa,
            retryAction = viewModel::loadSiswa,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    statusUiSiswa: StatusUiSiswa,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        when (statusUiSiswa) {

            is StatusUiSiswa.Loading -> {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }

            is StatusUiSiswa.Success -> {
                DaftarSiswa(
                    itemSiswa = statusUiSiswa.siswa
                )
            }

            is StatusUiSiswa.Error -> {
                ErrorScreen(
                    retryAction = retryAction,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.gagal),
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun DaftarSiswa(
    itemSiswa: List<DataSiswa>,
    modifier: Modifier = Modifier
) {
    if (itemSiswa.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.deskripsi_no_item
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        LazyColumn(modifier = modifier) {

            items(
                items = itemSiswa,
                key = { it.id }
            ) { person ->

                ItemSiswa(
                    siswa = person,
                    modifier = Modifier.padding(
                        dimensionResource(
                            id = R.dimen.padding_small
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun ItemSiswa(
    siswa: DataSiswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(
                dimensionResource(
                    id = R.dimen.padding_large
                )
            ),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(
                    id = R.dimen.padding_small
                )
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = siswa.nama,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = siswa.telpon,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = siswa.alamat,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}