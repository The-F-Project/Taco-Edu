;(function(e) {
    var matches = e.matches || e.matchesSelector || e.webkitMatchesSelector || e.mozMatchesSelector || e.msMatchesSelector || e.oMatchesSelector;
    !matches ? (e.matches = e.matchesSelector = function matches(selector) {
        var matches = document.querySelectorAll(selector);
        var th = this;
        return Array.prototype.some.call(matches, function(e) {
            return e === th;
        });
    }) : (e.matches = e.matchesSelector = matches);
})(Element.prototype);

(function(){
	var layout = document.querySelector('.layout'),
		sidebar = document.querySelector('.layout__sidebar'),
		content = document.querySelector('.layout__content'),
		hamburger = document.querySelector('.hamburger'),
		subjects = document.querySelectorAll('.subject'),
		nav = document.querySelector('.nav'),
		navIsOpen = false;

	hamburger.addEventListener('click', function(){
		if(navIsOpen) {
			this.classList.remove('is-active');
			content.classList.remove('layout__content_push');
			navIsOpen = false;
			setTimeout(function(){
				layout.classList.remove('layout_open');
				sidebar.classList.remove('layout__sidebar_open');
			}, 500);
		} else {
			this.classList.add('is-active');
			sidebar.classList.add('layout__sidebar_open');
			layout.classList.add('layout_open');
			content.classList.add('layout__content_push');
			navIsOpen = true;
		}
	});

	subjects.forEach(function(item){
		item.addEventListener('click', toggleSubject);
	});

	function toggleSubject() {
		var activeSubject;

		if(this.classList.contains('subject_open')) {
			this.classList.remove('subject_open');
		} else {
			activeSubject = document.querySelector('.subject_open');
			activeSubject && activeSubject.classList.remove('subject_open');
			this.classList.add('subject_open');
		}
	}

	function toggleNav() {
		this.classList.toggle('nav__btn_active');
	}

	nav && nav.addEventListener('click', function(e) {
		for (var target = e.target; target && target != this; target = target.parentNode) {
			if (target.matches('.nav__btn')) {
				toggleNav.call(target, e);
				break;
			}
		}
	});

	if(window.SmoothScroll) new SmoothScroll('.contents__link',{updateURL: false});

	document.querySelectorAll('.nav__link').forEach(function(item){
		var href = item.getAttribute('href');

		if(href === location.pathname || (typeof uCozCatUrl !== 'undefined' && uCozCatUrl === href)) {
			item.classList.add('nav__link_active');
			toggleNav.call(item.closest('.nav__list').previousElementSibling);
			return;
		}
	});

	var vkShareButtonCnt = document.getElementById('vk_share_button');
	var vkApiTransportCnt = document.getElementById("vk_api_transport");

	function vkShareInit(){
		var url = false;

		if(typeof canonicalUrl !== 'undefined') url = canonicalUrl;
		vkShareButtonCnt.innerHTML = VK.Share.button(url, {type: "round", text: "В закладки"});
	}

	function vkWidgetsInit() {
		var commentsOptions = {limit: 10, width: "", attach: "*", autoPublish: 1};
		var likeOptions = {type: "button", height: 30};

		if(typeof canonicalUrl !== 'undefined') {
			commentsOptions.pageUrl = canonicalUrl;
			likeOptions.pageUrl = canonicalUrl;
		}

		VK.init({apiId: 2737477, onlyWidgets: true});
		VK.Widgets.Like("vk_like", likeOptions);
		VK.Widgets.Comments("vk_comments", commentsOptions);
	}

	setTimeout(function() {
		if(vkApiTransportCnt) {
			var openapi = document.createElement("script");
			openapi.src = "https://vk.com/js/api/openapi.js?168";
			openapi.async = true;
			openapi.onload = vkWidgetsInit;
			vkApiTransportCnt.appendChild(openapi);
		}

		if(vkShareButtonCnt) {
			var share = document.createElement("script");
			share.src = "https://vk.com/js/api/share.js?93";
			share.async = true;
			share.onload = vkShareInit;
			document.head.append(share);
		}
	}, 5000);
})();

(function() {
	var task = document.querySelector('.task');
	var search = document.querySelector('.task-search__query');
	var taskLinks = document.querySelectorAll('.task__link');

	function spoilerToggle() {
		this.closest('.task__section').classList.toggle('task__section_expanded');
	}

	function filterTasks() {
		var query = this.value;

		taskLinks.forEach(function(item){
			if(item.textContent === query || !query) {
				item.style.display = 'block';
			} else {
				item.style.display = 'none';
			}
		});
	}

	if(task) {
		document.addEventListener('click', function(e) {
			for (var target = e.target; target && target != this; target = target.parentNode) {
				if (target.matches('.task__tab')) {
					spoilerToggle.call(target, e);
					break;
				}
			}
		});
	}

	if(search) search.addEventListener('keyup', filterTasks);
})();