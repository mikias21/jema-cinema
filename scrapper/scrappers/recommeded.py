from bs4 import BeautifulSoup
import requests_cache
import uuid 

# make cached request to the website
session = requests_cache.CachedSession('api_session')
content = None 
for i in range(30):
    request = session.get("https://vidcloud9.com/recommended-series")
    content = request.content 
    # soup 
    soup = BeautifulSoup(content, "html.parser")


def get_recommended():
    recommended = soup.select("#main_bg > div:nth-child(5) > div > div.vc_row.wpb_row.vc_row-fluid.vc_custom_1404913114846 > div.vc_col-sm-12.wpb_column.column_container > div > div > ul")
    recommended_titles = recommended[0].find_all("div", {"class": "name"})
    recommended_images = soup.select("#main_bg > div:nth-child(5) > div > div.vc_row.wpb_row.vc_row-fluid.vc_custom_1404913114846 > div.vc_col-sm-12.wpb_column.column_container > div > div > ul > li:nth-child(n) > a > div.img > div.picture > img")
    recommended_video = soup.select("#main_bg > div:nth-child(5) > div > div.vc_row.wpb_row.vc_row-fluid.vc_custom_1404913114846 > div.vc_col-sm-12.wpb_column.column_container > div > div > ul > li:nth-child(n) > a")
    recommended_uploaded = recommended[0].find_all("span", {"class": "date"})

    # Get recommended data
    recommended_main = []
    for title, image, video, uploaded in zip(
        recommended_titles,
        recommended_images,
        recommended_video,
        recommended_uploaded):

        recommended_main.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents().strip(),
                "image": image['src'],
                "video": video['href'],
                "uploaded_time": uploaded.decode_contents().strip(),
            }
        )
    return recommended_main

if __name__ == "__main__":
    print(get_recommended())