    const addToLibraryButtons = document.querySelectorAll(".btn[name='book']");

    addToLibraryButtons.forEach((button) => {
      button.addEventListener("click", () => {
        const book = button.value;
        const requestBody = book;

        fetch("/addBook", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: requestBody
        })
          .then((response) => response.text())
          .then((data) => {
              console.log(data);
              const alertContainer = document.createElement("div");
              alertContainer.classList.add("alert-container");

              const alertBox = document.createElement("div");
              alertBox.classList.add("alert-box");
              alertBox.innerText = data;

              const closeButton = document.createElement("button");
              closeButton.classList.add("close-button");
              closeButton.innerText = "x";
              closeButton.addEventListener("click", () => {
                  alertContainer.remove();
              });

              alertBox.appendChild(closeButton);
              alertContainer.appendChild(alertBox);
              document.body.appendChild(alertContainer);

              // Change background color based on success flag
              if (data.includes("success")) {
                alertBox.style.backgroundColor = "#c3e6cb"; // green color
              } else {
                alertBox.style.backgroundColor = "#f8d7da"; // red color
              }

              setTimeout(() => {
                  alertContainer.remove();
              }, 3000);
          })
          .catch((error) => console.error(error));
      });
    });

    const bookResults = document.querySelectorAll(".book-result");

    bookResults.forEach((bookResult) => {
      const thumbnail = bookResult.querySelector(".book-thumbnail");
      thumbnail.style.maxWidth = "100px";
      thumbnail.style.maxHeight = "100px";

      const title = bookResult.querySelector(".book-title");
      title.classList.add("card-title");

      const author = bookResult.querySelector(".book-author");
      author.classList.add("card-text");

      const isbn = bookResult.querySelector(".book-isbn");
      isbn.classList.add("card-text");

      const button = bookResult.querySelector(".btn[name='book']");
      button.classList.add("btn-primary");
      button.classList.add("w-100");
    });