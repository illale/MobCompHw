package com.mobile.computing.hw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobile.computing.hw1.ui.theme.HW1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HW1Theme {
                Surface(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
                    Conversation(CrowTitConversation.conversation)
                }
            }
        }
    }
}

data class Message(val author: String, val body: String , val img: Image)
data class Image(val resource: Int)

@Composable
fun MessageCard(msg: Message) {

    Row (modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(msg.img.resource),
            contentDescription = "Tintin",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "Surface color"
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.large,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

object CrowTitConversation {
    // Sample conversation data
    val conversation = listOf(
        Message(
            "Crow",
            """Caw! Hey, Tint! You're looking bright today. 
               What's it like being a ray of sunshine?
            """.trimIndent(),
            Image(R.drawable.raven)
        ),
        Message(
            "Tit",
            """Oh, thank you, Crow! It’s pretty amazing. 
               I get to travel all over the world in seconds, spreading 
               warmth and light. How about you? What’s it like soaring in the sky?
            """.trimIndent().trim(),
            Image(R.drawable.tintin)
        ),
        Message(
            "Crow",
            """Not bad, not bad. I get a bird’s-eye view of everything. But tell me, how do you 
               manage to shine so brightly every single day? Doesn’t it get exhausting?
            """.trimMargin().trim(),
            Image(R.drawable.raven)
        ),
        Message(
            "Tit",
            """
                Well, I don’t do it alone. There’s a whole team of us rays working together. 
                The Sun makes sure we’ve got plenty of energy. What about you? 
                How do you stay so clever? Always solving puzzles and finding shiny things.
            """.trimIndent(),
            Image(R.drawable.tintin)
        ),
        Message(
            "Crow",
            """Oh, that’s just part of being a crow. We’re natural-born thinkers. 
               You should see the tricks I’ve taught myself! Found a whole stash of snacks
               yesterday by opening a latch.
            """.trimMargin().trim(),
            Image(R.drawable.raven)
        ),
        Message(
            "Tit",
            """Impressive! You’re a little genius, aren’t you? 
               But don’t you ever get distracted by all the shiny things you find?
            """.trimIndent(),
            Image(R.drawable.tintin)
        ),
        Message(
            "Crow",
            """Well, sometimes. Shiny things are irresistible, you know. 
               Like that sparkle on the river this morning—was that you?
            """.trimIndent(),
            Image(R.drawable.raven)
        ),
        Message(
            "Tit",
            """Guilty as charged! I love playing with water; 
               it’s like painting on a moving canvas. Did it catch your eye?
            """.trimIndent(),
            Image(R.drawable.tintin)
        ),
        Message(
            "Crow",
            """It did. Almost dropped my snack just to watch it. You and water sure make a great team.
            """.trimIndent(),
            Image(R.drawable.raven)
        ),
        Message(
            "Tit",
            """And you make the world more interesting, Crow. Always curious, always watching. So, what’s your next adventure?
            """.trimIndent(),
            Image(R.drawable.tintin)
        ),
        Message(
            "Crow",
            """Not sure yet. Maybe I’ll follow your light and see where it takes me. Think you could lead me to something shiny?
            """.trimIndent(),
            Image(R.drawable.raven)
        ),
        Message(
            "Tit",
            "Of course! Follow me, clever crow. Let’s find some treasures together!".trimIndent(),
            Image(R.drawable.tintin)
        )
    )
}