package io.chaldeaprjkt.yumetsuki.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.constant.Source
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import io.chaldeaprjkt.yumetsuki.ui.dashboard.DashboardDestination
import io.chaldeaprjkt.yumetsuki.ui.hoyoweb.HoYoWebDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.Destination
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry
import io.chaldeaprjkt.yumetsuki.ui.theme.AppTheme
import io.chaldeaprjkt.yumetsuki.util.extension.grabTextClipboard
import io.chaldeaprjkt.yumetsuki.util.extension.hasTextClipboard
import io.chaldeaprjkt.yumetsuki.util.extension.trimQuotes
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    entry: NavEntry,
    cookie: String = "",
    viewModel: LoginViewModel,
    isAdding: Boolean = false,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(cookie) {
        if (cookie.isNotBlank()) {
            viewModel.login(cookie)
        }
    }
    LaunchedEffect(uiState) {
        if (uiState == LoginUiState.Done) {
            entry.switchTo(DashboardDestination)
        }
    }

    LoginContent(
        state = uiState,
        onLoginRequest = { viewModel.login(it) },
        onNavigate = entry::goTo,
        isAdding = isAdding,
        onDismissError = viewModel::resetState,
        onPopBack = { entry.pop() },
    )
}

@Composable
private fun PreviewScreen(isAdding: Boolean = false, uiState: LoginUiState = LoginUiState.Idle) {
    AppTheme {
        LoginContent(
            state = uiState,
            onLoginRequest = {},
            onNavigate = {},
            isAdding = isAdding,
            onDismissError = {},
            onPopBack = {},
        )
    }
}

@Preview
@Composable
private fun PreviewScreen_Login() {
    PreviewScreen()
}

@Preview
@Composable
private fun PreviewScreen_isAdding() {
    PreviewScreen(isAdding = true)
}

@Preview
@Composable
private fun PreviewScreen_isAdding_onLoading() {
    PreviewScreen(
        isAdding = true,
        uiState = LoginUiState.Loading(R.string.fetching_user_info),
    )
}

@Preview
@Composable
private fun PreviewScreen_isAdding_onErr() {
    PreviewScreen(
        isAdding = true,
        uiState = LoginUiState.Error(R.string.err_cookie_exists),
    )
}

@Composable
fun LoginContent(
    state: LoginUiState,
    onLoginRequest: (String) -> Unit,
    onNavigate: (Destination) -> Unit,
    onPopBack: () -> Unit,
    isAdding: Boolean,
    onDismissError: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (isAdding) {
                AddMoreAppBar(onPopBack = onPopBack)
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Column(
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(bottom = 24.dp)
                .consumeWindowInsets(padding),
            verticalArrangement = Arrangement.Bottom,
        ) {
            if (isAdding) {
                AddMoreUserContent(state)
            } else {
                WelcomeContent(state)
            }

            LoginStatus(state = state, onDismissError = onDismissError)

            if (state !is LoginUiState.Loading) {
                LoginActions(
                    onLoginRequest = onLoginRequest,
                    onNavigate = onNavigate,
                    onDismissError = onDismissError,
                )
            }
            if (!isAdding && state is LoginUiState.Idle) {
                FooterNotice()
            }
        }
    }
}

@Composable
fun AddMoreAppBar(onPopBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onPopBack) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = stringResource(id = android.R.string.cancel)
                )
            }
        },
        title = {},
    )
}

@Composable
fun WelcomeContent(state: LoginUiState) {
    Column(Modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.login_welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        AnimatedVisibility(visible = state is LoginUiState.Idle) {
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.login_welcome_desc),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
fun AddMoreUserContent(state: LoginUiState) {
    Column(Modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.add_more_user),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        AnimatedVisibility(visible = state is LoginUiState.Idle) {
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.add_more_user_desc),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
fun LoginStatus(state: LoginUiState, onDismissError: () -> Unit) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            (slideInHorizontally() + fadeIn() togetherWith
                    slideOutHorizontally { w -> w / 2 + w } + fadeOut())
                .using(SizeTransform(clip = false))
        },
        label = "LoginStatusAnim",
    ) { result ->
        when (result) {
            is LoginUiState.Error -> {
                Row(
                    modifier =
                        Modifier.padding(vertical = 16.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.errorContainer),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = result.messageId),
                        style = MaterialTheme.typography.labelSmall,
                        modifier =
                            Modifier.weight(1f).padding(start = 24.dp).padding(vertical = 8.dp),
                    )
                    IconButton(onClick = onDismissError) {
                        Icon(Icons.Filled.Close, contentDescription = null)
                    }
                }
            }
            is LoginUiState.Loading -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier.padding(horizontal = 16.dp, vertical = 24.dp).fillMaxWidth(),
                ) {
                    Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(id = result.messageId),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            else -> Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun LoginActions(
    modifier: Modifier = Modifier,
    onLoginRequest: (String) -> Unit,
    onNavigate: (Destination) -> Unit,
    onDismissError: () -> Unit,
) {
    val enterCookieDialogOpen = remember { mutableStateOf(false) }
    val grabCookieDialogOpen = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (enterCookieDialogOpen.value) {
        EnterCookieDialog(
            onDismiss = { enterCookieDialogOpen.value = false },
            onSuccess = {
                enterCookieDialogOpen.value = false
                coroutineScope.launch { onLoginRequest(it) }
            },
        )
    }

    if (grabCookieDialogOpen.value) {
        GrabCookieDialog(
            onDismiss = { grabCookieDialogOpen.value = false },
            onNavigate = onNavigate,
        )
    }

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        FilledTonalButton(
            onClick = {
                onDismissError()
                grabCookieDialogOpen.value = true
            },
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            val iconSize = 16.dp
            Image(
                painterResource(id = R.drawable.ic_hoyolab),
                modifier = Modifier.size(iconSize),
                contentDescription = null,
            )
            Text(
                stringResource(R.string.get_hoyolab_cookie),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilledTonalButton(
            onClick = {
                onDismissError()
                enterCookieDialogOpen.value = true
            },
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            val iconSize = 16.dp
            Icon(
                imageVector = Icons.Outlined.Code,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.enter_cookie),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GrabCookieDialog(onDismiss: () -> Unit, onNavigate: (Destination) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Image(
                painterResource(id = R.drawable.ic_hoyolab),
                modifier = Modifier.size(32.dp),
                contentDescription = null,
            )
        },
        title = { Text(stringResource(id = R.string.native_hoyolab_title)) },
        text = { Text(stringResource(id = R.string.native_hoyolab_account_msg)) },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onNavigate(HoYoWebDestination)
                },
            ) {
                Text(stringResource(id = R.string.open_hoyolab))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onNavigate(SNSHelpDestination)
                },
            ) {
                Text(stringResource(id = R.string.sns_account))
            }
        },
    )
}

@Composable
fun EnterCookieDialog(
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit,
) {
    val context = LocalContext.current
    val hasClipboard = context.hasTextClipboard()
    var textField by remember { mutableStateOf(TextFieldValue()) }
    var fieldHintId by remember { mutableIntStateOf(R.string.hint_cookie) }

    LaunchedEffect(textField.text) {
        val cookie = textField.text.trimQuotes()
        fieldHintId =
            if (cookie.contains("(ltuid|ltoken)".toRegex()) && HoYoCookie(cookie).isValid()) {
                onSuccess(cookie)
                return@LaunchedEffect
            } else if (cookie.isBlank()) {
                R.string.hint_cookie
            } else {
                R.string.err_cookie_invalid_short
            }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            modifier = Modifier.padding(24.dp).wrapContentSize().animateContentSize()
        ) {
            Column(
                modifier = Modifier.sizeIn(minWidth = 280.dp, maxWidth = 560.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Outlined.Cookie,
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
                Text(
                    stringResource(id = R.string.enter_cookie),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                OutlinedTextField(
                    label = { Text(text = stringResource(id = fieldHintId)) },
                    maxLines = 2,
                    value = textField,
                    onValueChange = { textField = it },
                    isError = textField.text.isNotBlank(),
                    modifier =
                        Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 16.dp),
                )

                if (hasClipboard) {
                    HorizontalDivider()
                    TextButton(
                        shape = RectangleShape,
                        onClick = {
                            context.grabTextClipboard()?.let {
                                textField = TextFieldValue(it.trimQuotes())
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.paste_from_clipboard),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FooterNotice() {
    val uriHandler = LocalUriHandler.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 16.dp),
    ) {
        HighlightedText(
            text = stringResource(id = R.string.footer_disclaimer),
            highlights =
                listOf(
                    Highlight(
                        text = stringResource(id = R.string.disclaimer),
                        data = Source.App.Disclaimer,
                        onClick = uriHandler::openUri,
                    ),
                    Highlight(
                        text = stringResource(id = R.string.privacy_policy),
                        data = Source.App.PrivacyPolicy,
                        onClick = uriHandler::openUri,
                    ),
                ),
            style =
                MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
        )
    }
}

data class Highlight(val text: String, val data: String, val onClick: (data: String) -> Unit)

@Composable
fun HighlightedText(
    text: String,
    highlights: List<Highlight>,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    highlightColor: Color = MaterialTheme.colorScheme.secondary,
) {
    data class TextData(
        val text: String,
        val tag: String? = null,
        val data: String? = null,
        val onClick: ((data: AnnotatedString.Range<String>) -> Unit)? = null
    )

    val textData = mutableListOf<TextData>()
    if (highlights.isEmpty()) {
        textData.add(TextData(text = text))
    } else {
        var startIndex = 0
        highlights.forEachIndexed { i, link ->
            val endIndex = text.indexOf(link.text)
            if (endIndex == -1) {
                throw Exception("Highlighted text mismatch")
            }
            textData.add(TextData(text = text.substring(startIndex, endIndex)))
            textData.add(
                TextData(
                    text = link.text,
                    tag = "${link.text}_TAG",
                    data = link.data,
                    onClick = { link.onClick(it.item) }
                )
            )
            startIndex = endIndex + link.text.length
            if (i == highlights.lastIndex && startIndex < text.length) {
                textData.add(TextData(text = text.substring(startIndex, text.length)))
            }
        }
    }

    val annotatedString = buildAnnotatedString {
        textData.forEach { linkTextData ->
            if (linkTextData.tag != null && linkTextData.data != null) {
                pushStringAnnotation(
                    tag = linkTextData.tag,
                    annotation = linkTextData.data,
                )
                withStyle(
                    style = SpanStyle(color = highlightColor, fontWeight = FontWeight.SemiBold),
                ) {
                    append(linkTextData.text)
                }
                pop()
            } else {
                append(linkTextData.text)
            }
        }
    }
    ClickableText(
        text = annotatedString,
        style = style ?: MaterialTheme.typography.labelSmall,
        onClick = { offset ->
            textData.forEach { annotatedStringData ->
                if (annotatedStringData.tag != null && annotatedStringData.data != null) {
                    annotatedString
                        .getStringAnnotations(
                            tag = annotatedStringData.tag,
                            start = offset,
                            end = offset,
                        )
                        .firstOrNull()
                        ?.let { annotatedStringData.onClick?.invoke(it) }
                }
            }
        },
        modifier = modifier
    )
}
