document.addEventListener('DOMContentLoaded', function () {
    const languageDropdown = document.getElementById('locales');
    const currentLang = localStorage.getItem('language');

    if (currentLang) {
        languageDropdown.value = currentLang;
    }

    languageDropdown.addEventListener('change', (event) => {
       const selectedLang = event.target.value;

       if (selectedLang) {
           localStorage.setItem('language', selectedLang);
           window.location.href = window.location.pathname + '?lang=' + selectedLang;
       }
    });
});