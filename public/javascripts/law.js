function lawInit() {
    console.log( "lawInit")

    var law = new Vue({
          el: '#lawdiff-ajax-content',

          data: {
            bodyContent: null,
            currVer: null
          },

          methods: {
            // seed with current version
            seedLaw: function() {
                var element = document.getElementById('important-data');
                var ver = element.getAttribute('data-data');
                this.currVer = ver
                console.log( "seedLaw: " + this.currVer )
                LawScroll.setup_click_handlers()
            },

            loadLaw: function ( law, ver ) {
                console.log( "loadLaw: " + this.currVer + " -> " + ver )
                if( this.currVer == ver ) {
                    return // do nothing
                }

                this.toggleVer()
                this.currVer = ver
                this.toggleVer()

                var url = '/raw/likums/'+law+'/v/'+ver
                var href = '/likums/'+law+'/v/'+ver

                console.log("loadLaw: GET " + url )

                var self = this
                $.get( url, function(data) {
                    self.hideServerSideContent()

                    self.bodyContent = data
                    Vue.nextTick( function() { lawInit(ver) } )

                    window.history.pushState( null, null, href )

                })
            },

            // mark (or unmark) active law version css
            toggleVer: function() {
                if( this.currVer ) {
                    $('#'+ this.currVer ).toggleClass('active');
                }
            },

            // before we do ajax replace, we want to completely remove something that was pre-rendered server side
            hideServerSideContent: function() {
                $('#lawdiff-server-side').remove()
            }

          }

        })

    law.seedLaw()
    return law
}

var LawScroll = {

    NoMoreChangesMsg: 'Vairāk izmaiņu nav',

    css_offset_y: 100,

    // is called after law content is loaded (via ajax function)
    setup_click_handlers: function() {
        $('span[change-id]').each( function() {
            var el = $( this )

            el.click( LawScroll.scroll_to )

        } );
    },

    scroll_to: function( event ) {
        var clicked = $( this )
        var pos1 = clicked.offset().top // y position of this element
        console.log( clicked.attr('id') + " -> " + clicked.text() + " // pos: " + pos1 )

        var el = clicked
        while( el != null ) {
            var next = el.attr('next')
            if( next == null ) break
            if( next == 'last-change' ) {
                LawScroll.popoverLastChange( clicked )
                break;
            }

            var nextEl = $('#'+next) // if has attribute, find the relevant span element
            console.log( next + " => " + nextEl )
            if( nextEl != null ) {
                var elPos = nextEl.offset().top
                console.log( " . el pos: " + elPos +", el offset: " + document.getElementById(next).offsetTop )
                if( elPos > pos1 + 40 ) {
                    var container = $(window)
                    var elOffset = nextEl.offset().top - 40 - LawScroll.css_offset_y
                    console.log( "scroll cur: " + container.scrollTop() + ", " + next + " -> " + elOffset )
                    container.scrollTop( elOffset )
                    console.log( " . now: " + container.scrollTop()  )

                    break
                }
            }

            el = nextEl // keep searching, need to find one that is substantially further than this
        }
    },

    // notify user that there is nothing else to see
    popoverLastChange: function( el ) {
        el.removeAttr( 'title' )
            .attr( 'data-toggle', 'popover' )
            .attr( 'data-content', LawScroll.NoMoreChangesMsg )
            .popover( 'show' )

        setTimeout( function () { el.popover('destroy') }, 2000 )
    },

    scroll_prev: function( top_el ) {
        var prev_for = top_el.getAttribute('prevchange');
        var el = $(prev_for);
        var pos1 = Element.cumulativeOffset(el)[1]; // y position of this element
        while( el != null )
        {
            var prev = el.readAttribute('previous');
            if( prev == null ) return;
            if( prev == 'first-change' ) {
                // no attribute, means nowhere to go - show tooltip
                finalChangeHint( top_el );
                return;
            }

            prev = $(prev); // if has attribute, find the relevant span element
            if( prev != null ) {
                var pos2 = Element.cumulativeOffset(prev)[1];
                if( pos2 < pos1 - 40 ) { // check if previous change is somewhat significantly above this
                    prev.scrollTo(); // ... and scroll to it
                    return;
                }
            }

            el = prev;
        }
    },

    scroll_first_change: function() {
        $("span[previous='first-change']").scrollTo();
    }


}

$(document).ready(function(){
    lawInit();
})
