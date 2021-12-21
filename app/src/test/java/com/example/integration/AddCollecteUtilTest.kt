package com.example.integration

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

class AddCollecteUtilTest{



    @Test
    fun `empty name returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "",
            "1234567890000",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `empty description returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `empty localisation returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "1234567890000",
            "",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `empty url returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "1234567890000",
            "Louvain-la-Neuve",
            "Arthur",
            "",
            "10/12",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `empty date returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "1234567890000",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `empty heure returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "1234567890000",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            ""
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `invalid description returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "pas bon",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `invalid date returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "ca c'est correct",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "100/12",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `invalid heure returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "ca c'est correct",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "100:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `invalid format date returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "ca c'est correct",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "100:12",
            "10:10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `invalid format heure returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "ca c'est correct",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "10/10"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `valid collecte returns true`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte Test",
            "ca c'est correct",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "10:10"
        )
        assertThat(result).isTrue()
    }
    @Test
    fun `name already taken returns false`(){
        val result = AddCollecteUtil.validateAddCollecteInput(
            "Collecte LLN",
            "ca c'est correct",
            "Louvain-la-Neuve",
            "Arthur",
            "https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608_960_720.jpg",
            "10/12",
            "10:10"
        )
        assertThat(result).isFalse()
    }

}