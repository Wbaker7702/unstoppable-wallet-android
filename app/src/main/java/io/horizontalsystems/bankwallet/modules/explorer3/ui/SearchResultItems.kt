package io.horizontalsystems.bankwallet.modules.explorer3.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchResult
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.Body_leah
import io.horizontalsystems.bankwallet.ui.compose.components.SectionItemBorderedRowUniversalClear
import io.horizontalsystems.bankwallet.ui.compose.components.Subhead2_grey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionResultItem(
    result: SearchResult.TransactionResult,
    onClick: () -> Unit
) {
    SectionItemBorderedRowUniversalClear(
        borderTop = true,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_transactions),
                contentDescription = null,
                tint = ComposeAppTheme.colors.jacob,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Body_leah(
                    text = result.hash.take(16) + "..." + result.hash.takeLast(8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Subhead2_grey(
                    text = result.subtitle ?: result.blockchainType.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                result.timestamp?.let { timestamp ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Subhead2_grey(
                        text = formatTimestamp(timestamp),
                        maxLines = 1
                    )
                }
            }
            
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up_right_24),
                contentDescription = null,
                tint = ComposeAppTheme.colors.grey,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun AddressResultItem(
    result: SearchResult.AddressResult,
    onClick: () -> Unit
) {
    SectionItemBorderedRowUniversalClear(
        borderTop = true,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qr_scan_24px),
                contentDescription = null,
                tint = ComposeAppTheme.colors.jacob,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Body_leah(
                    text = result.address.take(12) + "..." + result.address.takeLast(8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Subhead2_grey(
                    text = result.subtitle ?: result.blockchainType.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                result.balance?.let { balance ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Subhead2_grey(
                        text = "Balance: $balance",
                        maxLines = 1
                    )
                }
            }
            
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up_right_24),
                contentDescription = null,
                tint = ComposeAppTheme.colors.grey,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun BlockResultItem(
    result: SearchResult.BlockResult,
    onClick: () -> Unit
) {
    SectionItemBorderedRowUniversalClear(
        borderTop = true,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null,
                tint = ComposeAppTheme.colors.jacob,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Body_leah(
                    text = result.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Subhead2_grey(
                    text = result.subtitle ?: result.blockchainType.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                result.timestamp?.let { timestamp ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Subhead2_grey(
                        text = formatTimestamp(timestamp),
                        maxLines = 1
                    )
                }
            }
            
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up_right_24),
                contentDescription = null,
                tint = ComposeAppTheme.colors.grey,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun TokenResultItem(
    result: SearchResult.TokenResult,
    onClick: () -> Unit
) {
    SectionItemBorderedRowUniversalClear(
        borderTop = true,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Token icon placeholder - can be enhanced with actual token images
            Icon(
                painter = painterResource(R.drawable.coin_placeholder),
                contentDescription = null,
                tint = ComposeAppTheme.colors.grey50,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Body_leah(
                    text = result.symbol,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Subhead2_grey(
                    text = result.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up_right_24),
                contentDescription = null,
                tint = ComposeAppTheme.colors.grey,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ContractResultItem(
    result: SearchResult.ContractResult,
    onClick: () -> Unit
) {
    SectionItemBorderedRowUniversalClear(
        borderTop = true,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null,
                tint = ComposeAppTheme.colors.jacob,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Body_leah(
                    text = result.address.take(12) + "..." + result.address.takeLast(8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Subhead2_grey(
                    text = result.subtitle ?: result.blockchainType.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                result.isVerified?.let { verified ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Subhead2_grey(
                        text = if (verified) "Verified" else "Unverified",
                        maxLines = 1
                    )
                }
            }
            
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up_right_24),
                contentDescription = null,
                tint = ComposeAppTheme.colors.grey,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
