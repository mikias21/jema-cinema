from bs4 import BeautifulSoup   
import requests_cache
import uuid


# make cached request to the website
session = requests_cache.CachedSession('api_session')
content = None 
for i in range(30):
    request = session.get("https://www.flixtor.video/home")
    content = request.content 
    # soup 
    soup = BeautifulSoup(content, "html.parser")

MOVIE_URL = "https://flixtor.video"

def get_recommended_content():
    # home movie content 
    recommended = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(1) > div")
    recommended_titles =  recommended[0].find_all("a", {"class": "title"})
    recommended_quality = recommended[0].find_all("div", {"class": "quality"})
    recommended_poster = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(1) > div > div:nth-child(n) > a > img")
    recommended_video = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(1) > div > div:nth-child(n) > a")
    recommended_rate = recommended[0].find_all("span", {"class": "imdb"})
    recommended_year = recommended[0].find_all("div", {"class": "meta"})
    recommended_type = recommended[0].find_all("i", {"class": "type"})

    # Get recommended data
    recommended_main = []
    for title, quality, poster, video, rate, year, type in zip(
        recommended_titles,
        recommended_quality,
        recommended_poster,
        recommended_video,
        recommended_rate,
        recommended_year,
        recommended_type,):

        recommended_main.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents(),
                "quality": quality.decode_contents(),
                "poster": poster['src'],
                "video": video["href"],
                "rate": rate.getText(),
                "year": year.getText().split(" ")[1],
                "type": type.getText(),
            }
        )
    return recommended_main

def get_recommended_tv_shows():
    recommeded_tv = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(2) > div")
    recommended_tv_titles =  recommeded_tv[0].find_all("a", {"class": "title"})
    recommended_tv_quality = recommeded_tv[0].find_all("div", {"class": "quality"})
    recommended_tv_poster = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(2) > div > div:nth-child(n) > a > img")
    recommended_tv_video = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(2) > div > div:nth-child(n) > a")
    recommended_tv_rate = recommeded_tv[0].find_all("span", {"class": "imdb"})
    recommended_tv_year = recommeded_tv[0].find_all("div", {"class": "meta"})
    recommended_tv_type = recommeded_tv[0].find_all("i", {"class": "type"})

    # Get recommended data
    recommended_tv = []
    for title, quality, poster, video, rate, year, type in zip(
        recommended_tv_titles,
        recommended_tv_quality,
        recommended_tv_poster,
        recommended_tv_video,
        recommended_tv_rate,
        recommended_tv_year,
        recommended_tv_type,):

        recommended_tv.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents(),
                "quality": quality.decode_contents(),
                "poster": poster['src'],
                "video": video["href"],
                "rate": rate.getText(),
                "year": year.getText().split(" ")[1],
                "type": type.getText(),
            }
        )
    return recommended_tv


def get_trending():
    trending = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(3) > div")
    trending_titles =  trending[0].find_all("a", {"class": "title"})
    trending_quality = trending[0].find_all("div", {"class": "quality"})
    trending_poster = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(3) > div > div:nth-child(n) > a > img")
    trending_video = soup.select("#body > div.container.mt-5 > section:nth-child(2) > div.content > div:nth-child(3) > div > div:nth-child(n) > a")
    trending_rate = trending[0].find_all("span", {"class": "imdb"})
    trending_year = trending[0].find_all("div", {"class": "meta"})
    trending_type = trending[0].find_all("i", {"class": "type"})

    # Get trending data
    trending_main = []
    for title, quality, poster, video, rate, year, type in zip(
        trending_titles,
        trending_quality,
        trending_poster,
        trending_video,
        trending_rate,
        trending_year,
        trending_type,):

        trending_main.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents(),
                "quality": quality.decode_contents(),
                "poster": poster['src'],
                "video": video["href"],
                "rate": rate.getText(),
                "year": year.getText().split(" ")[1],
                "type": type.getText(),
            }
        )
    return trending_main



def get_latest_movies():
    latest_movies = soup.select("#body > div.container.mt-5 > section:nth-child(3) > div.content > div")
    latest_movies_title = soup.select("#body > div.container.mt-5 > section:nth-child(3) > div.content > div > div:nth-child(n) > h3 > a")
    latest_movies_quality = soup.select("#body > div.container.mt-5 > section:nth-child(3) > div.content > div > div:nth-child(n) > div.icons > div")
    latest_movies_poster = soup.select("#body > div.container.mt-5 > section:nth-child(3) > div.content > div > div:nth-child(n) > a > img")
    latest_movies_vidoes = soup.select("#body > div.container.mt-5 > section:nth-child(3) > div.content > div > div:nth-child(n) > a")
    latest_movies_rate = latest_movies[0].find_all("span", {"class": "imdb"})
    latest_movies_year = latest_movies[0].find_all("div", {"class": "meta"})
    latest_movies_type = latest_movies[0].find_all("i", {"class": "type"})

    # Get latest movies data
    latest_main = []
    for title, quality, poster, video, rate, year, type in zip(
        latest_movies_title,
        latest_movies_quality,
        latest_movies_poster,
        latest_movies_vidoes,
        latest_movies_rate,
        latest_movies_year,
        latest_movies_type):

        latest_main.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents(),
                "quality": quality.decode_contents(),
                "poster": poster['src'],
                "video": video["href"],
                "rate": rate.getText(),
                "year": year.getText().split(" ")[1],
                "type": type.getText(),
            }
        )

    return latest_main

def get_latest_tv():
    latest_tv = soup.select("#body > div.container.mt-5 > section:nth-child(4) > div.content > div")
    latest_tv_title = soup.select("#body > div.container.mt-5 > section:nth-child(4) > div.content > div > div:nth-child(n) > h3 > a")
    latest_tv_quality = soup.select("#body > div.container.mt-5 > section:nth-child(4) > div.content > div > div:nth-child(n) > div.icons > div")
    latest_tv_poster = soup.select("#body > div.container.mt-5 > section:nth-child(4) > div.content > div > div:nth-child(n) > a > img")
    latest_tv_vidoes = soup.select("#body > div.container.mt-5 > section:nth-child(4) > div.content > div > div:nth-child(n) > a")
    latest_tv_rate = latest_tv[0].find_all("span", {"class": "imdb"})
    latest_tv_year = latest_tv[0].find_all("div", {"class": "meta"})
    latest_tv_type = latest_tv[0].find_all("i", {"class": "type"})

    # Get latest movies data
    latest_tv = []
    for title, quality, poster, video, rate, year, type in zip(
        latest_tv_title,
        latest_tv_quality,
        latest_tv_poster,
        latest_tv_vidoes,
        latest_tv_rate,
        latest_tv_year,
        latest_tv_type):

        latest_tv.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents(),
                "quality": quality.decode_contents(),
                "poster": poster['src'],
                "video": video["href"],
                "rate": rate.getText(),
                "year": year.getText().split(" ")[1],
                "type": type.getText(),
            }
        )

    return latest_tv


if __name__ == "__main__":
    print(get_trending())



# https://mcloud.to/embed/j6j9vn?sub.info=https%3A%2F%2Fflixtor.video%2Fajax%2Fepisode%2Fsubtitles%2Fe8504359bd0eca30b831867bf20d670d%3F&autostart=true
# https://mcloud.to/embed/13n64m?sub.info=https%3A%2F%2Fflixtor.video%2Fajax%2Fepisode%2Fsubtitles%2F04f7e3bb3edbd5b0f265ab0be3bfc052%3F&autostart=true
