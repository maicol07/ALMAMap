import autoAnimate from 'https://cdn.jsdelivr.net/npm/@formkit/auto-animate'

//        let currentAngle = 0;
//        const target = document.body.firstElementChild;
//        const zt = new ZingTouch.Region(target, undefined, false);
//        zt.bind(target).rotate(function(e) {
//          currentAngle += e.detail.distanceFromLast;
//          document.body.style.transform = 'rotate(' + currentAngle + 'deg)';
//        });

autoAnimate(document.body);
autoAnimate(document.body.querySelector('#content'));

/**
 * Load HTML content into the page
 * @param html {string} - The HTML content to load
 */
function loadHTML(html) {
    const content = document.querySelector('#content');
    if (!content) {
        console.error('No content element found');
        return;
    }
    console.log('Loading HTML content');
    content.innerHTML = html;

    // Add click event listeners to all elements with an ID
    const parts = document.querySelectorAll('#content svg [id]');
    for (const part of parts) {
        part.addEventListener('click', function(event) {
            window.kmpJsBridge.callNative('onMapClick', part.id);
        });
    }
}
window.loadHTML = loadHTML;

/**
 * Add a marker to the map
 * @param id {string} - The ID of the element to add the marker to
 * @param svgD {string} - The SVG path data for the marker
 */
function addMarkerTo(id, svgD) {
    const element = document.querySelector(`#${id}`);
    if (element) {
        console.log('Adding marker to element', element.id);
        const rect = element.getBoundingClientRect();
        const marker = document.createElement('div');
        marker.id = `${id.replaceAll('\\.', '.')}-marker`;
        marker.className = 'marker';
        marker.innerHTML = `<svg class="icon" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="${svgD}"></path></svg>`;
        marker.style.top = rect.top + (rect.height / 2) + 'px';
        marker.style.left = rect.left + (rect.width / 2) + 'px';
        const svg = marker.querySelector('svg');
        svg.setAttribute('fill', 'currentColor')
        // Remove stroke attributes
        svg.removeAttribute('stroke')
        svg.removeAttribute('stroke-linecap')
        svg.removeAttribute('stroke-linejoin')
        svg.removeAttribute('stroke-width')
        marker.addEventListener('click', function(event) {
            window.kmpJsBridge.callNative('onMapClick', element.id);
        });
        document.body.appendChild(marker);
    } else {
        console.warn('Element not found', id);
    }
}
window.addMarkerTo = addMarkerTo;

/**
 * Delete a marker from the map
 * @param id {string} - The ID of the marker
 */
function deleteMarker(id) {
    const marker = document.querySelector(`#${id}-marker`);
    marker?.remove();
}
window.deleteMarker = deleteMarker;

/**
 * Clear all markers from the map
 */
function clearMarkers() {
    const markers = document.querySelectorAll('.marker');
    for (const marker of markers) {
        marker.remove();
    }
}
window.clearMarkers = clearMarkers;

console.log('Map script loaded');
