# Simple visualization
Below is the structure of data save in firestore:  

root  
|_user  
|_news  
|   |_contents  
|   |_images  
|_comments  

## comments's attributes:
- authorUsername: author's username of the article in which comment is written.
- title: title of the article in which comment is written.
- username: comment's writer username
- donwnvote: amount of disagreement
- upvote: amount of agreement
- comment: comment's content

## user's attributes:
- username: username of a user
- password: user's password
- name: name of a user
Below attributes is for future enhancement.
- isAdmin: is user a admin.
- isWriter: is user a writer.

## new's:
### attributes:
- authorUsername: author's username of the article.
- title: title of the article.
- type: types of article.
- createedDate: the day of writing.
- previewContent: short paragraph for preview.
- thumbnailURL: url of thumbnail.

## collections:
Purpose of these collections is reduce data usage to when don't need to view full article.
contents: list of paragraphs, split from article contents.
Furthermore, it help for future features, which is allow add image between 2 paragraphs.
images: list of image's url in article.